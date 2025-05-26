import React from "react";
import { Button, TextField, CircularProgress } from "@mui/material";
import { useFormik } from "formik";
import * as Yup from "yup";
import { register } from "../services/auth"; 

const RegisterForm = ({ onBack }) => {
  const [loading, setLoading] = React.useState(false);
  const [errorMsg, setErrorMsg] = React.useState("");
  const [success, setSuccess] = React.useState(false);

  const formik = useFormik({
    initialValues: { username: "", email: "", password: "" },
    validationSchema: Yup.object({
      username: Yup.string().min(2, "Trop court").required("Obligatoire"),
      email: Yup.string().email("Email invalide").required("Obligatoire"),
      password: Yup.string().min(6, "6 caractères minimum").required("Obligatoire"),
    }),
    onSubmit: async (values) => {
      setLoading(true);
      setErrorMsg("");
      setSuccess(false);
      try {
        await register(values);
        setSuccess(true);
        setTimeout(() => {
          window.location.href = "/boards";
        }, 1500);
      } catch (e) {
        setErrorMsg("Erreur lors de l’inscription ou email déjà utilisé.");
      }
      setLoading(false);
    },
  });

  return (
    <form onSubmit={formik.handleSubmit} style={{ display: "flex", flexDirection: "column", gap: 16 }}>
      <h2 style={{ marginBottom: 12, color: "#fff", fontWeight: 700 }}>Créer un compte</h2>
      <TextField
        label="Nom d'utilisateur"
        name="username"
        variant="outlined"
        fullWidth
        size="small"
        autoFocus
        value={formik.values.username}
        onChange={formik.handleChange}
        error={!!formik.touched.username && !!formik.errors.username}
        helperText={formik.touched.username && formik.errors.username}
        sx={{
            input: { color: "#dfdfdf" },
            label: { color: "#dfdfdf" },
            // --- Bordure normale
            "& .MuiOutlinedInput-root": {
            "& fieldset": {
                borderColor: "#fff"
            },
            // --- Bordure au hover
            "&:hover fieldset": {
                borderColor: "#b983fe"
            },
            // --- Bordure au focus
            "&.Mui-focused fieldset": {
                borderColor: "#fff",
                borderWidth: 2
            },

            borderRadius: 2
            },
            // --- HelperText en blanc aussi
            "& .MuiFormHelperText-root": { color: "#fff" }
        }}
      />
      <TextField
        label="Email"
        name="email"
        variant="outlined"
        fullWidth
        size="small"
        value={formik.values.email}
        onChange={formik.handleChange}
        error={!!formik.touched.email && !!formik.errors.email}
        helperText={formik.touched.email && formik.errors.email}
        sx={{
            input: { color: "#dfdfdf" },
            label: { color: "#dfdfdf" },
            // --- Bordure normale
            "& .MuiOutlinedInput-root": {
            "& fieldset": {
                borderColor: "#fff"
            },
            // --- Bordure au hover
            "&:hover fieldset": {
                borderColor: "#b983fe"
            },
            // --- Bordure au focus
            "&.Mui-focused fieldset": {
                borderColor: "#fff",
                borderWidth: 2
            },

            borderRadius: 2
            },
            // --- HelperText en blanc aussi
            "& .MuiFormHelperText-root": { color: "#fff" }
        }}
      />
      <TextField
        label="Mot de passe"
        name="password"
        type="password"
        variant="outlined"
        fullWidth
        size="small"
        value={formik.values.password}
        onChange={formik.handleChange}
        error={!!formik.touched.password && !!formik.errors.password}
        helperText={formik.touched.password && formik.errors.password}
        sx={{
            input: { color: "#dfdfdf" },
            label: { color: "#dfdfdf" },
            // --- Bordure normale
            "& .MuiOutlinedInput-root": {
            "& fieldset": {
                borderColor: "#fff"
            },
            // --- Bordure au hover
            "&:hover fieldset": {
                borderColor: "#b983fe"
            },
            // --- Bordure au focus
            "&.Mui-focused fieldset": {
                borderColor: "#fff",
                borderWidth: 2
            },

            borderRadius: 2
            },
            // --- HelperText en blanc aussi
            "& .MuiFormHelperText-root": { color: "#fff" }
        }}
      />
      {errorMsg && <div style={{ color: "#e57373", fontWeight: 500 }}>{errorMsg}</div>}
      {success && <div style={{ color: "#4caf50", fontWeight: 500 }}>Inscription réussie ! Redirection...</div>}
      <Button
        type="submit"
        variant="contained"
        size="large"
        sx={{
          mt: 2,
          borderRadius: 99,
          fontWeight: 700,
          background: "linear-gradient(90deg, #8f4bfc 0%, #6e8efb 100%)"
        }}
        disabled={loading}
      >
        {loading ? <CircularProgress size={22} sx={{ color: "#fff" }} /> : "S’inscrire"}
      </Button>
      <Button
        onClick={onBack}
        variant="text"
        size="small"
        sx={{ mt: 0.5, color: "#b983fe", fontWeight: 500 }}
        disabled={loading}
      >
        ← Retour à l’accueil
      </Button>
    </form>
  );
};

export default RegisterForm;
