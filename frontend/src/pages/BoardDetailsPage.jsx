import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Box, Typography, CircularProgress, Button, Paper, Grid, TextField, Dialog, DialogActions, DialogContent
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import PhotoCameraIcon from '@mui/icons-material/PhotoCamera';
import DeleteIcon from '@mui/icons-material/Delete';
import { getToken } from "../services/auth";

import { uploadTaskImage } from "../services/taskImage";
import { deleteTask } from "../services/task";
import { deleteListColumn } from "../services/listColumn";
import { DragDropContext, Droppable, Draggable } from "@hello-pangea/dnd";


const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";

const BoardDetailsPage = () => {
  const { id } = useParams(); // L’id du board dans l’URL
  const navigate = useNavigate();

  

  // State
  const [board, setBoard] = useState(null);
  const [columns, setColumns] = useState([]);
  const [loading, setLoading] = useState(true);
  const [columnDialog, setColumnDialog] = useState(false);
  const [newColumnName, setNewColumnName] = useState("");
  const [taskDialog, setTaskDialog] = useState({ open: false, colId: null });
  const [newTaskTitle, setNewTaskTitle] = useState("");
  const [refresh, setRefresh] = useState(false);
  const [uploadingTaskId, setUploadingTaskId] = useState(null);

  const handleImageUpload = async (taskId, file) => {
  setUploadingTaskId(taskId);
  try {
    await uploadTaskImage(taskId, file);
    setRefresh(r => !r); // Pour recharger les tâches avec image
  } catch {
    alert("Erreur lors de l’upload de l’image.");
  }
  setUploadingTaskId(null);
};

  // Charge board + colonnes
  useEffect(() => {
    const fetchBoardAndColumns = async () => {
      setLoading(true);
      try {
        // 1. Charge le board
        const boardRes = await fetch(`${API_URL}/api/boards/${id}`, {
          headers: { Authorization: "Bearer " + getToken() }
        });
        if (!boardRes.ok) throw new Error("Board non trouvé");
        const boardData = await boardRes.json();
        setBoard(boardData);

        // 2. Charge les colonnes (et leurs tâches)
        const columnsRes = await fetch(`${API_URL}/api/list-columns/board/${id}`, {
          headers: { Authorization: "Bearer " + getToken() }
        });
        const columnsData = await columnsRes.json();

        // On peut aussi ajouter les tâches dans chaque colonne
        // (Ou alors, endpoint qui ramène tout, à adapter)
        for (const col of columnsData) {
            const tasksRes = await fetch(`${API_URL}/api/tasks/list-column/${col.id}`, {
                headers: { Authorization: "Bearer " + getToken() }
                });
                let tasks = await tasksRes.json();

            col.tasks = tasks;
            }

        setColumns(columnsData);

      } catch (e) {
        navigate("/boards");
      }
      setLoading(false);
    };
    fetchBoardAndColumns();
    // Refresh si ajout
  }, [id, refresh, navigate]);

  // Ajouter une colonne
  const handleAddColumn = async () => {
    if (!newColumnName.trim()) return;
    await fetch(`${API_URL}/api/list-columns`, {
      method: "POST",
      headers: {
        Authorization: "Bearer " + getToken(),
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ name: newColumnName, board: { id: board.id } })
    });
    setNewColumnName("");
    setColumnDialog(false);
    setRefresh(r => !r);
  };

  // Ajouter une tâche dans une colonne
  const handleAddTask = async () => {
    if (!newTaskTitle.trim()) return;
    await fetch(`${API_URL}/api/tasks`, {
      method: "POST",
      headers: {
        Authorization: "Bearer " + getToken(),
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        title: newTaskTitle,
        description: "",
        listColumn: { id: taskDialog.colId }
      })
    });
    setNewTaskTitle("");
    setTaskDialog({ open: false, colId: null });
    setRefresh(r => !r);
  };
  // Delete task 
  const handleDeleteTask = async (taskId) => {
        if (!window.confirm("Supprimer cette tâche ?")) return;
        try {
            await deleteTask(taskId);
            setRefresh(r => !r); // recharge les tâches
        } catch {
            alert("Erreur lors de la suppression.");
        }
    };

    // delete colonne
        const handleDeleteColumn = async (colId) => {
    if (!window.confirm("Supprimer cette colonne et toutes ses tâches ?")) return;
    try {
        await deleteListColumn(colId);
        setRefresh(r => !r); // recharge les colonnes/tâches
    } catch {
        alert("Erreur lors de la suppression de la colonne.");
    }
    };

    // Drag & drop 
    const handleDragEnd = async (result) => {
        const { destination, source, draggableId } = result;

        // Si l’utilisateur drop hors zone ou au même endroit, rien à faire
        if (!destination ||
            (destination.droppableId === source.droppableId &&
            destination.index === source.index)) {
            return;
        }

        // Trouve la colonne source et destination
        const sourceColIdx = columns.findIndex(c => c.id.toString() === source.droppableId);
        const destColIdx = columns.findIndex(c => c.id.toString() === destination.droppableId);

        if (sourceColIdx === -1 || destColIdx === -1) return;

        // Copie les colonnes (immutabilité)
        const newColumns = [...columns];
        const [movedTask] = newColumns[sourceColIdx].tasks.splice(source.index, 1);
        newColumns[destColIdx].tasks.splice(destination.index, 0, movedTask);

        // Met à jour visuellement
        setColumns(newColumns);

        // MAJ back : PATCH ou PUT la task avec son nouveau listColumnId
        try {
            await fetch(`${API_URL}/api/tasks/${movedTask.id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + getToken(),
            },
            body: JSON.stringify({
                ...movedTask,
                listColumn: { id: columns[destColIdx].id }
            }),
            });
        } catch {
            // (optionnel: tu peux rollback si erreur)
            alert("Erreur lors du déplacement !");
        }
        };


  if (loading) return (
    <Box sx={{ minHeight: "100vh", display: "flex", alignItems: "center", justifyContent: "center", background: "linear-gradient(135deg, #1f2235 0%, #2a2546 100%)" }}>
      <CircularProgress sx={{ color: "#b983fe" }} />
    </Box>
  );

  if (!board) return null; // Sécurité

  return (
    <Box sx={{
      minHeight: "100vh",
      background: "linear-gradient(135deg, #1f2235 0%, #2a2546 100%)",
      p: 3,
      overflowX: "auto"
    }}>
      <Box sx={{ mb: 3, display: "flex", alignItems: "center" }}>
        <Button onClick={() => navigate("/boards")} sx={{ color: "#b983fe", mr: 2 }}>← Mes Tableaux</Button>
        <Typography variant="h4" fontWeight={700} color="#b983fe">{board.name}</Typography>
        <Typography sx={{ ml: 3, color: "#a9a9bc", fontSize: 18 }}>({columns.length} colonnes)</Typography>
      </Box>

      <Button
        variant="outlined"
        sx={{ borderRadius: 8, color: "#b983fe", borderColor: "#b983fe", mb: 2 }}
        startIcon={<AddIcon />}
        onClick={() => setColumnDialog(true)}
      >
        Nouvelle colonne
      </Button>

    <DragDropContext onDragEnd={handleDragEnd}>
        <Grid container spacing={2} sx={{ flexWrap: "nowrap", overflowX: "auto", pb: 6 }}>
            {columns.map((col, colIdx) => (
            <Grid item key={col.id} sx={{ minWidth: 300, maxWidth: 320 }}>
                <Paper sx={{
                background: "#23233a",
                borderRadius: 5,
                p: 2,
                minHeight: 420,
                boxShadow: "0 2px 16px #b983fe22",
                display: "flex",
                flexDirection: "column"
                }}>
                <Typography variant="h6" fontWeight={700} color="#b983fe" gutterBottom>
                    {col.name}
                    <DeleteIcon
                    sx={{ ml: 1, cursor: "pointer", fontSize: 20, color: "#e57373" }}
                    onClick={() => handleDeleteColumn(col.id)}
                    titleAccess="Supprimer la colonne"
                    />
                </Typography>

                <Droppable droppableId={col.id.toString()} type="task">
                    {(provided) => (
                    <Box
                        ref={provided.innerRef}
                        {...provided.droppableProps}
                        sx={{ flex: 1, minHeight: 40 }}
                    >
                        {col.tasks && col.tasks.length > 0 ? (
                        col.tasks.map((task, taskIdx) => (
                            <Draggable key={task.id} draggableId={task.id.toString()} index={taskIdx}>
                            {(provided, snapshot) => (
                                <Paper
                                ref={provided.innerRef}
                                {...provided.draggableProps}
                                {...provided.dragHandleProps}
                                sx={{
                                    p: 1.2, mb: 1.4, borderRadius: 3, background: "#342b54", color: "#fff",
                                    boxShadow: snapshot.isDragging
                                    ? "0 6px 24px #b983fe77"
                                    : "0 1px 6px #b983fe14",
                                    opacity: snapshot.isDragging ? 0.92 : 1,
                                    transition: "box-shadow .2s"
                                }}
                                >
                                <Typography fontWeight={600}>{task.title}</Typography>
                                {task.description && (
                                    <Typography fontSize={14} color="#aaa">{task.description}</Typography>
                                )}
                                <Typography fontWeight={600} sx={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
                                    <DeleteIcon
                                    sx={{ ml: 1, cursor: "pointer", fontSize: 21, color: "#e57373" }}
                                    onClick={() => handleDeleteTask(task.id)}
                                    />
                                </Typography>
                                {/* Affiche l’image si présente */}
                                {task.imageUrl && task.imageUrl.length > 0 && (
                                    <Box sx={{ mt: 1 }}>
                                    <img
                                        src={API_URL + task.imageUrl}
                                        alt="Aperçu tâche"
                                        style={{
                                        maxWidth: "100%",
                                        maxHeight: 120,
                                        borderRadius: 10,
                                        display: "block",
                                        marginBottom: 5,
                                        objectFit: "cover",
                                        boxShadow: "0 2px 8px #0001"
                                        }}
                                    />
                                    </Box>
                                )}

                                {/* Bouton upload image avec icône */}
                                <label style={{ cursor: "pointer", marginTop: 5, display: "inline-block" }}>
                                    <input
                                    type="file"
                                    accept="image/png,image/jpeg,image/webp"
                                    style={{ display: "none" }}
                                    disabled={!!uploadingTaskId}
                                    onChange={e => {
                                        if (e.target.files && e.target.files[0]) {
                                        handleImageUpload(task.id, e.target.files[0]);
                                        }
                                    }}
                                    />
                                    <Button
                                    size="small"
                                    variant="text"
                                    sx={{ color: "#b983fe", borderRadius: 6, fontWeight: 500, minWidth: 0 }}
                                    disabled={!!uploadingTaskId}
                                    component="span"
                                    >
                                    {uploadingTaskId === task.id
                                        ? "Envoi..."
                                        : <PhotoCameraIcon sx={{ fontSize: 21, color: "#b983fe" }} />}
                                    </Button>
                                </label>
                                </Paper>
                            )}
                            </Draggable>
                        ))
                        ) : (
                        <Typography color="#aaa" fontSize={15}>Aucune tâche</Typography>
                        )}
                        {provided.placeholder}
                    </Box>
                    )}
                </Droppable>
                <Button
                    variant="text"
                    size="small"
                    sx={{ color: "#b983fe", borderRadius: 6, fontWeight: 500, mt: 1 }}
                    onClick={() => setTaskDialog({ open: true, colId: col.id })}
                >
                    + Ajouter tâche
                </Button>
                </Paper>
            </Grid>
            ))}
        </Grid>
    </DragDropContext>


      {/* Dialog nouvelle colonne */}
      <Dialog open={columnDialog} onClose={() => setColumnDialog(false)}>
        <DialogContent>
          <Typography variant="h6" fontWeight={700}>Nouvelle colonne</Typography>
          <TextField
            label="Nom de la colonne"
            value={newColumnName}
            onChange={e => setNewColumnName(e.target.value)}
            fullWidth
            autoFocus
            sx={{ mt: 2 }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setColumnDialog(false)} color="secondary">Annuler</Button>
          <Button
            onClick={handleAddColumn}
            disabled={!newColumnName.trim()}
            variant="contained"
            sx={{ background: "linear-gradient(90deg, #8f4bfc 0%, #6e8efb 100%)" }}
          >
            Créer
          </Button>
        </DialogActions>
      </Dialog>

      {/* Dialog nouvelle tâche */}
      <Dialog open={taskDialog.open} onClose={() => setTaskDialog({ open: false, colId: null })}>
        <DialogContent>
          <Typography variant="h6" fontWeight={700}>Nouvelle tâche</Typography>
          <TextField
            label="Titre de la tâche"
            value={newTaskTitle}
            onChange={e => setNewTaskTitle(e.target.value)}
            fullWidth
            autoFocus
            sx={{ mt: 2 }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setTaskDialog({ open: false, colId: null })} color="secondary">Annuler</Button>
          <Button
            onClick={handleAddTask}
            disabled={!newTaskTitle.trim()}
            variant="contained"
            sx={{ background: "linear-gradient(90deg, #8f4bfc 0%, #6e8efb 100%)" }}
          >
            Créer
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default BoardDetailsPage;
