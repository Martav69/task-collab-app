import React from "react";
import { Button, TextField, CircularProgress } from "@mui/material";
import { useFormik } from "formik";
import * as Yup from "yup";
import { login } from "../services/auth";

const LoginForm = ({ onBack }) => {
  const [loading, setLoading] = React.useState(false);
  const [errorMsg, setErrorMsg] = React.useState("");

  const formik = useFormik({
    initialValues: { email: "", password: "" },
    validationSchema: Yup.object({
      email: Yup.string().email("Email invalide").required("Obligatoire"),
      password: Yup.string().required("Obligatoire"),
    }),
    onSubmit: async (values) => {
      setLoading(true);
      setErrorMsg("");
      try {
        const token = await login(values.email, values.password);
        localStorage.setItem("token", token);
        window.location.href = "/boards"; 
      } catch (e) {
        setErrorMsg("Email ou mot de passe incorrect");
      }
      setLoading(false);
    },
  });

  return (
    <form onSubmit={formik.handleSubmit} style={{ display: "flex", flexDirection: "column", gap: 16 }}>
      <h2 style={{ marginBottom: 12, color: "#fff", fontWeight: 700 }}>Connexion</h2>
      <TextField
        label="Email"
        name="email"
        variant="outlined"
        fullWidth
        size="small"
        autoFocus
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
        {loading ? <CircularProgress size={22} sx={{ color: "#fff" }} /> : "Se connecter"}
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

export default LoginForm;
