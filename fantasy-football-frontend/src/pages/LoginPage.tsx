"use client"
import type React from "react"
import { useState } from "react"
import { useNavigate } from "react-router-dom"
import styled, { keyframes } from "styled-components"

// Animations
const fadeInUp = keyframes`
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
`

const slideInScale = keyframes`
  from {
    opacity: 0;
    transform: scale(0.9) translateY(20px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
`

const gradientShift = keyframes`
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
`

const shake = keyframes`
  0%, 100% {
    transform: translateX(0);
  }
  10%, 30%, 50%, 70%, 90% {
    transform: translateX(-5px);
  }
  20%, 40%, 60%, 80% {
    transform: translateX(5px);
  }
`

const float = keyframes`
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-10px);
  }
`

// Styled Components
const Container = styled.div`
  min-height: 100vh;
  background: linear-gradient(-45deg, #0a2e0a, #0b3d0b, #1a4a1a, #0f3f0f);
  background-size: 400% 400%;
  animation: ${gradientShift} 15s ease infinite;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #e8f5e8;
  padding: 2rem;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: radial-gradient(circle at 20% 80%, rgba(46, 184, 46, 0.1) 0%, transparent 50%),
                radial-gradient(circle at 80% 20%, rgba(31, 122, 31, 0.1) 0%, transparent 50%);
    pointer-events: none;
  }

  &::after {
    content: '';
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: repeating-linear-gradient(
      45deg,
      transparent,
      transparent 2px,
      rgba(168, 213, 168, 0.03) 2px,
      rgba(168, 213, 168, 0.03) 4px
    );
    animation: float 20s ease-in-out infinite;
    pointer-events: none;
  }
`

const LoginCard = styled.div`
  background: rgba(18, 44, 18, 0.8);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(168, 213, 168, 0.2);
  border-radius: 25px;
  padding: 3rem;
  width: 100%;
  max-width: 420px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.4);
  animation: ${slideInScale} 0.8s ease-out;
  position: relative;
  z-index: 1;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, rgba(168, 213, 168, 0.1) 0%, transparent 50%);
    border-radius: 25px;
    pointer-events: none;
  }

  @media (max-width: 768px) {
    padding: 2rem;
    margin: 1rem;
  }
`

const Logo = styled.div`
  text-align: center;
  margin-bottom: 2rem;
  animation: ${fadeInUp} 1s ease-out 0.2s both;
`

const LogoIcon = styled.div`
  font-size: 4rem;
  margin-bottom: 0.5rem;
  animation: float 3s ease-in-out infinite;
`

const LogoText = styled.h1`
  margin: 0;
  font-size: 2rem;
  font-weight: 800;
  background: linear-gradient(135deg, #a8d5a8, #2db82d);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: -0.5px;
`

const Title = styled.h2`
  margin: 0 0 2rem 0;
  font-size: 2rem;
  font-weight: 700;
  text-align: center;
  color: #ffffff;
  animation: ${fadeInUp} 1s ease-out 0.4s both;

  @media (max-width: 768px) {
    font-size: 1.5rem;
  }
`

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  animation: ${fadeInUp} 1s ease-out 0.6s both;
`

const InputGroup = styled.div`
  position: relative;
`

const InputLabel = styled.label`
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
  color: #a8d5a8;
  font-size: 0.9rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
`

const Input = styled.input`
  width: 100%;
  padding: 1rem 1.2rem;
  border-radius: 15px;
  border: 2px solid rgba(168, 213, 168, 0.2);
  font-size: 1rem;
  background: rgba(255, 255, 255, 0.05);
  color: #ffffff;
  transition: all 0.3s ease;
  box-sizing: border-box;

  &::placeholder {
    color: rgba(255, 255, 255, 0.5);
  }

  &:focus {
    outline: none;
    border-color: #2db82d;
    background: rgba(255, 255, 255, 0.1);
    box-shadow: 0 0 20px rgba(45, 184, 45, 0.2);
    transform: translateY(-2px);
  }

  &:hover {
    border-color: rgba(168, 213, 168, 0.4);
  }
`

const Button = styled.button`
  background: linear-gradient(135deg, #1f7a1f, #2db82d);
  color: white;
  padding: 1rem 2rem;
  font-size: 1.1rem;
  font-weight: 600;
  border: none;
  border-radius: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 6px 25px rgba(31, 122, 31, 0.3);
  position: relative;
  overflow: hidden;
  text-transform: uppercase;
  letter-spacing: 0.5px;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
    transition: left 0.5s;
  }

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 30px rgba(31, 122, 31, 0.4);
    
    &::before {
      left: 100%;
    }
  }

  &:active {
    transform: translateY(-1px);
  }

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    transform: none;
  }
`

const ErrorMsg = styled.div<{ show: boolean }>`
  color: #ff6b6b;
  font-weight: 600;
  text-align: center;
  padding: 1rem;
  background: rgba(255, 107, 107, 0.1);
  border: 1px solid rgba(255, 107, 107, 0.3);
  border-radius: 10px;
  margin-bottom: 1rem;
  animation: ${(props) => (props.show ? shake : "none")} 0.5s ease-in-out;
  backdrop-filter: blur(10px);
`

const ForgotPassword = styled.a`
  color: #a8d5a8;
  text-decoration: none;
  text-align: center;
  font-size: 0.9rem;
  margin-top: 1rem;
  transition: all 0.3s ease;

  &:hover {
    color: #2db82d;
    text-decoration: underline;
  }
`

const SignUpPrompt = styled.div`
  text-align: center;
  margin-top: 2rem;
  padding-top: 2rem;
  border-top: 1px solid rgba(168, 213, 168, 0.2);
  color: #c8e6c8;
  animation: ${fadeInUp} 1s ease-out 0.8s both;
`

const SignUpLink = styled.a`
  color: #2db82d;
  text-decoration: none;
  font-weight: 600;
  margin-left: 0.5rem;
  transition: all 0.3s ease;

  &:hover {
    color: #a8d5a8;
    text-decoration: underline;
  }
`

const LoadingSpinner = styled.div`
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-right: 0.5rem;

  @keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
  }
`

interface LoginPageProps {
  onLogin?: (userData: { username: string; token: string }) => void
}

function LoginPage({ onLogin }: LoginPageProps) {
  const [username, setUsername] = useState("")
  const [password, setPassword] = useState("")
  const [error, setError] = useState("")
  const [isLoading, setIsLoading] = useState(false)
  const navigate = useNavigate()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)
    setError("")

    const loginPayload = { username, password }

    try {
      const response = await fetch("http://localhost:7070/fantasyfootball/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(loginPayload),
      })

      const data = await response.json()

      if (!response.ok) {
        setError(data.msg || "Login mislykkedes - prøv igen.")
        setIsLoading(false)
        return
      }

      localStorage.setItem("token", data.token)
      localStorage.setItem("username", data.username)

      if (onLogin) {
        onLogin({ username: data.username, token: data.token })
      }

      navigate("/dashboard")
    } catch (err) {
      console.error(err)
      setError("Der opstod en fejl under login. Prøv igen senere.")
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <Container>
      <LoginCard>
        <Logo>
          <LogoIcon>⚽</LogoIcon>
          <LogoText>FantasyFootball</LogoText>
        </Logo>

        <Title>Velkommen tilbage!</Title>

        {error && <ErrorMsg show={!!error}>{error}</ErrorMsg>}

        <Form onSubmit={handleSubmit}>
          <InputGroup>
            <InputLabel htmlFor="username">Brugernavn</InputLabel>
            <Input
              id="username"
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Indtast dit brugernavn"
              required
              disabled={isLoading}
            />
          </InputGroup>

          <InputGroup>
            <InputLabel htmlFor="password">Adgangskode</InputLabel>
            <Input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Indtast din adgangskode"
              required
              disabled={isLoading}
            />
          </InputGroup>

          <Button type="submit" disabled={isLoading}>
            {isLoading && <LoadingSpinner />}
            {isLoading ? "Logger ind..." : "Log ind"}
          </Button>
        </Form>

        <ForgotPassword href="#" onClick={(e) => e.preventDefault()}>
          Glemt adgangskode?
        </ForgotPassword>

        <SignUpPrompt>
          Har du ikke en konto?
          <SignUpLink href="#" onClick={(e) => e.preventDefault()}>
            Opret konto
          </SignUpLink>
        </SignUpPrompt>
      </LoginCard>
    </Container>
  )
}

export default LoginPage
