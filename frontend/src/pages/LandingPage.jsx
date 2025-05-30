import React, { useState } from "react";
import styles from "./LandingPage.module.css";
import { Button } from "@mui/material";
import logo from "../assets/logo.png";
import heroSvg from "../assets/hero.svg";

import GroupIcon from "@mui/icons-material/Group";
import SecurityIcon from "@mui/icons-material/Security";
import ImageIcon from "@mui/icons-material/Image";
import BoltIcon from "@mui/icons-material/Bolt";

import LoginForm from "../components/LoginForm";
import RegisterForm from "../components/RegisterForm";

const LandingPage = () => { 

  const [cardView, setCardView] = useState("landing");
  
  return (
      <div className={styles.root}>
      {/* image */}
      <img src={heroSvg} alt="Collaboration visuelle" className={styles.heroImage} />

       {/* Card centrale */}
      <div className={styles.card}>
        <img src={logo} alt="Logo" className={styles.logo} />
        {cardView === "landing" && (
          <>
            <div className={styles.title}>Taskul</div>
            <div className={styles.slogan}>
              Organisez vos tâches <span style={{ color: "#8f4bfc", fontWeight: 600 }}>en solo</span>, simplement et rapidement.
            </div>
            <div className={styles.buttonGroup}>
              <Button
                variant="contained"
                size="large"
                sx={{
                  borderRadius: 99,
                  fontWeight: 700,
                  background: "linear-gradient(90deg, #8f4bfc 0%, #6e8efb 100%)",
                  boxShadow: "0 3px 12px #4e206f22",
                }}
                onClick={() => setCardView("login")}
              >
                Se connecter
              </Button>
              <Button
                variant="outlined"
                size="large"
                sx={{
                  borderRadius: 99,
                  fontWeight: 700,
                  color: "#b983fe",
                  border: "2px solid #a777e3",
                  "&:hover": {
                    border: "2.2px solid #fff",
                    background: "#7f62a72b",
                  },
                }}
                onClick={() => setCardView("register")}
              >
                Créer un compte
              </Button>
            </div>
          </>
        )}
        {cardView === "login" && (
          <LoginForm onBack={() => setCardView("landing")} />
        )}
        {cardView === "register" && (
          <RegisterForm onBack={() => setCardView("landing")} />
        )}
      </div>

      {/* Features */}
      <div className={styles.features}>
        <div><GroupIcon /> Collaboration temps réel</div>
        <div><SecurityIcon /> Sécurité avancée</div>
        <div><ImageIcon /> Upload d’images</div>
        <div><BoltIcon /> Rapide & Gratuit</div>
      </div>

      {/* Footer confiance */}
      <footer className={styles.footer}>
        © 2024 Task Collab App • Fait avec <b>React</b> & <b>Spring Boot</b>
        <a href="https://github.com/martav69/task-collab-app" target="_blank" rel="noopener noreferrer">
          Voir sur GitHub
        </a>
      </footer>
    </div>
  );
};

export default LandingPage;
