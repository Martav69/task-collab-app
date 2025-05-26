import React, { useEffect, useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import {
  Button, Card, CardContent, Typography, CircularProgress, Grid, Box, Dialog, DialogContent, TextField, DialogActions
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import GroupIcon from "@mui/icons-material/Group";
import DashboardIcon from "@mui/icons-material/Dashboard";
import { getToken, isAuthenticated } from "../services/auth";
import { AuthContext } from "../context/AuthContext";


const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";

const BoardsPage = () => {
  const navigate = useNavigate();
  const [boards, setBoards] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState("");
  const [showDialog, setShowDialog] = useState(false);
  const [newBoardName, setNewBoardName] = useState("");
  const [createError, setCreateError] = useState("");
  const [creating, setCreating] = useState(false);

  const { user } = useContext(AuthContext);


  // Charge les boards
  const fetchBoards = async () => {
    setLoading(true);
    setErrorMsg("");
    try {
      const res = await fetch(`${API_URL}/api/boards`, {
        headers: {
          Authorization: "Bearer " + getToken(),
          "Content-Type": "application/json",
        },
      });
      if (!res.ok) throw new Error("Erreur lors du chargement des boards");
      const data = await res.json();
      setBoards(data);
    } catch (e) {
      setErrorMsg("Impossible de charger vos tableaux.");
    }
    setLoading(false);
  };

  useEffect(() => {
    if (!isAuthenticated()) {
      navigate("/");
      return;
    }
    fetchBoards();
  }, [navigate]);

  

  // Création d’un nouveau board
  const handleCreateBoard = async () => {
    setCreateError("");
    if (!newBoardName.trim()) return;
    setCreating(true);
    try {
      const res = await fetch(`${API_URL}/api/boards`, {
        method: "POST",
        headers: {
          Authorization: "Bearer " + getToken(),
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name: newBoardName.trim(),
          owner: { id: user?.id }
        }),
      });
      if (!res.ok) throw new Error("Erreur lors de la création du tableau");
      setShowDialog(false);
      setNewBoardName("");
      fetchBoards();
    } catch {
      setCreateError("Impossible de créer le tableau.");
    }
    setCreating(false);
  };

  return (
    <Box sx={{
      minHeight: "100vh",
      background: "linear-gradient(135deg, #1f2235 0%, #2a2546 100%)",
      padding: "48px 0",
      color: "#fff",
      display: "flex",
      flexDirection: "column",
      alignItems: "center"
    }}>
      <Typography variant="h3" fontWeight={700} mb={2} color="#b983fe" sx={{ letterSpacing: 1 }}>
        Mes Tableaux <DashboardIcon sx={{ fontSize: 36, ml: 1 }} />
      </Typography>
      <Typography variant="subtitle1" mb={4} color="#b9a8fd">
        Gérez tous vos projets d’un coup d’œil.
      </Typography>

      {/* Bouton création board */}
      <Button
        startIcon={<AddIcon />}
        variant="contained"
        size="large"
        sx={{
          mb: 4,
          background: "linear-gradient(90deg, #8f4bfc 0%, #6e8efb 100%)",
          borderRadius: 99,
          fontWeight: 700,
          px: 4
        }}
        onClick={() => setShowDialog(true)}
      >
        Nouveau tableau
      </Button>

      {/* Dialog création board */}
      <Dialog open={showDialog} onClose={() => setShowDialog(false)}>
        <DialogContent sx={{ minWidth: 350 }}>
          <Typography variant="h6" fontWeight={600} mb={2}>Créer un tableau</Typography>
          <TextField
            label="Nom du tableau"
            value={newBoardName}
            onChange={e => setNewBoardName(e.target.value)}
            autoFocus
            fullWidth
            variant="outlined"
            sx={{
              mb: 2,
              input: { color: "#350045" },
              label: { color: "#b983fe" },
            }}
          />
          {createError && <Typography color="error" mb={1}>{createError}</Typography>}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setShowDialog(false)} sx={{ color: "#b983fe" }} disabled={creating}>Annuler</Button>
          <Button
            onClick={handleCreateBoard}
            variant="contained"
            disabled={!newBoardName.trim() || creating}
            sx={{
              background: "linear-gradient(90deg, #8f4bfc 0%, #6e8efb 100%)",
              borderRadius: 99,
              fontWeight: 700,
              minWidth: 110
            }}
          >
            {creating ? <CircularProgress size={18} sx={{ color: "#fff" }} /> : "Créer"}
          </Button>
        </DialogActions>
      </Dialog>

      {loading ? (
        <CircularProgress sx={{ color: "#b983fe", mt: 8 }} />
      ) : errorMsg ? (
        <Typography color="#e57373" fontWeight={600}>{errorMsg}</Typography>
      ) : (
        <Grid container spacing={3} justifyContent="center" sx={{ maxWidth: 1100, width: "100%" }}>
          {boards.length === 0 && (
            <Typography color="#b983fe" fontWeight={500} sx={{ textAlign: "center", width: "100%" }}>
              Aucun tableau créé. Cliquez sur “Nouveau tableau” pour commencer !
            </Typography>
          )}
          {boards.map(board => (
            <Grid item xs={12} sm={6} md={4} key={board.id}>
              <Card
                sx={{
                  background: "#2a2546",
                  borderRadius: 4,
                  boxShadow: "0 4px 20px #b983fe22",
                  transition: "transform .15s",
                  "&:hover": { transform: "scale(1.025)", cursor: "pointer", boxShadow: "0 8px 28px #b983fe44" }
                }}
                onClick={() => navigate(`/boards/${board.id}`)}
              >
                <CardContent>
                  <Typography variant="h5" fontWeight={600} color="#b983fe" gutterBottom>
                    {board.name}
                  </Typography>
                  <Typography color="#fff" mb={1} fontSize={16}>
                    <GroupIcon sx={{ mr: 1, fontSize: 20, verticalAlign: "middle" }} />
                    Collaborateurs : 1
                  </Typography>
                  <Typography color="#a9a9bc" fontSize={14}>
                    Créé par <b>{board.ownerUsername || board.ownerId || "?"}</b>
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}
    </Box>
  );
};

export default BoardsPage;
