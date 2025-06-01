import { Link, useNavigate } from "react-router-dom"
import styled, { keyframes } from "styled-components"
import TodaysMatchesTable from "../components/TodayMatchesTable"
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

const slideInLeft = keyframes`
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
`

const slideInRight = keyframes`
  from {
    opacity: 0;
    transform: translateX(30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
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

const pulse = keyframes`
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
`

const shimmer = keyframes`
  0% {
    background-position: -200px 0;
  }
  100% {
    background-position: calc(200px + 100%) 0;
  }
`

// Styled Components
const Container = styled.div`
  min-height: 100vh;
  background: linear-gradient(-45deg, #0a2e0a, #0b3d0b, #1a4a1a, #0f3f0f);
  background-size: 400% 400%;
  animation: ${gradientShift} 15s ease infinite;
  color: #e8f5e8;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow-x: hidden;

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
`

const Navbar = styled.nav`
  padding: 1.5rem 2rem;
  background: rgba(18, 44, 18, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(168, 213, 168, 0.2);
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  animation: ${slideInLeft} 0.8s ease-out;

  @media (max-width: 768px) {
    padding: 1rem;
    flex-direction: column;
    gap: 1rem;
  }
`

const Logo = styled.h1`
  margin: 0;
  font-size: 2rem;
  font-weight: 800;
  background: linear-gradient(135deg, #a8d5a8, #2db82d);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: -0.5px;
  
  @media (max-width: 768px) {
    font-size: 1.5rem;
  }
`

const NavLinks = styled.div`
  display: flex;
  align-items: center;
  gap: 1.5rem;
  animation: ${slideInRight} 0.8s ease-out;

  @media (max-width: 768px) {
    gap: 1rem;
    flex-wrap: wrap;
    justify-content: center;
  }
`

const NavLink = styled(Link)`
  color: #a8d5a8;
  font-weight: 600;
  text-decoration: none;
  padding: 0.5rem 1rem;
  border-radius: 25px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(168, 213, 168, 0.2), transparent);
    transition: left 0.5s;
  }

  &:hover {
    color: #ffffff;
    background: rgba(46, 184, 46, 0.2);
    transform: translateY(-2px);
    
    &::before {
      left: 100%;
    }
  }

  @media (max-width: 768px) {
    padding: 0.4rem 0.8rem;
    font-size: 0.9rem;
  }
`

const LogoutButton = styled.button`
  background: linear-gradient(135deg, #a82a2a, #e74c3c);
  border: none;
  color: white;
  padding: 0.6rem 1.2rem;
  border-radius: 25px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(168, 42, 42, 0.3);
  position: relative;
  overflow: hidden;

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
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(168, 42, 42, 0.4);
    
    &::before {
      left: 100%;
    }
  }

  &:active {
    transform: translateY(0);
  }

  @media (max-width: 768px) {
    padding: 0.5rem 1rem;
    font-size: 0.9rem;
  }
`

const Main = styled.main`
  flex: 1;
  padding: 3rem 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  z-index: 1;

  @media (max-width: 768px) {
    padding: 2rem 1rem;
  }
`

const WelcomeSection = styled.div`
  text-align: center;
  margin-bottom: 3rem;
  animation: ${fadeInUp} 1s ease-out;
`

const WelcomeTitle = styled.h2`
  font-size: 3rem;
  font-weight: 900;
  margin: 0 0 1rem 0;
  background: linear-gradient(135deg, #ffffff, #a8d5a8);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  line-height: 1.2;
  letter-spacing: -1px;

  @media (max-width: 768px) {
    font-size: 2rem;
  }
`

const WelcomeSubtitle = styled.p`
  font-size: 1.2rem;
  color: #c8e6c8;
  margin: 0;
  font-weight: 300;

  @media (max-width: 768px) {
    font-size: 1rem;
  }
`

const DashboardGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  width: 100%;
  max-width: 1200px;
  margin-bottom: 3rem;
  animation: ${fadeInUp} 1s ease-out 0.3s both;

  @media (max-width: 768px) {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
`

const DashboardCard = styled.div`
  background: rgba(18, 44, 18, 0.6);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(168, 213, 168, 0.2);
  border-radius: 20px;
  padding: 2rem;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: linear-gradient(45deg, transparent, rgba(168, 213, 168, 0.05), transparent);
    transform: rotate(45deg);
    transition: all 0.5s ease;
    opacity: 0;
  }
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 15px 40px rgba(0, 0, 0, 0.3);
    border-color: rgba(168, 213, 168, 0.4);

    &::before {
      opacity: 1;
      animation: ${shimmer} 1.5s ease-in-out;
    }
  }
`

const CardIcon = styled.div`
  font-size: 3rem;
  margin-bottom: 1rem;
  text-align: center;
`

const CardTitle = styled.h3`
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0 0 0.5rem 0;
  color: #a8d5a8;
  text-align: center;
`

const CardDescription = styled.p`
  font-size: 1rem;
  color: #c8e6c8;
  margin: 0 0 1.5rem 0;
  line-height: 1.5;
  text-align: center;
`

const CardButton = styled.button`
  width: 100%;
  background: linear-gradient(135deg, #1f7a1f, #2db82d);
  border: none;
  border-radius: 15px;
  color: white;
  padding: 0.8rem 1.5rem;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(31, 122, 31, 0.3);
  position: relative;
  overflow: hidden;

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
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(31, 122, 31, 0.4);
    
    &::before {
      left: 100%;
    }
  }

  &:active {
    transform: translateY(0);
  }
`

const QuickActionsSection = styled.div`
  width: 100%;
  max-width: 600px;
  animation: ${fadeInUp} 1s ease-out 0.6s both;
`

const QuickActionsTitle = styled.h3`
  font-size: 1.8rem;
  font-weight: 700;
  margin: 0 0 1.5rem 0;
  color: #a8d5a8;
  text-align: center;
`

const ButtonsContainer = styled.div`
  display: flex;
  justify-content: center;
  gap: 1.5rem;
  flex-wrap: wrap;
`

const ActionButton = styled.button`
  background: linear-gradient(135deg, #1f7a1f, #2db82d);
  border: none;
  border-radius: 50px;
  color: white;
  padding: 1rem 2rem;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 6px 25px rgba(31, 122, 31, 0.3);
  position: relative;
  overflow: hidden;
  animation: ${pulse} 2s ease-in-out infinite;

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
    transform: translateY(-3px) scale(1.05);
    box-shadow: 0 8px 30px rgba(31, 122, 31, 0.4);
    animation-play-state: paused;
    
    &::before {
      left: 100%;
    }
  }

  &:active {
    transform: translateY(-1px) scale(1.02);
  }

  @media (max-width: 768px) {
    padding: 0.8rem 1.5rem;
    font-size: 1rem;
  }
`



function DashboardPage({ onLogout, username = "Spiller" }) {
  const navigate = useNavigate()

  const handleLogout = () => {
    if (onLogout) {
      onLogout()
    }
    navigate("/login")
  }

  return (
    <Container>
      <Navbar>
        <Logo>FantasyFootball</Logo>
        <NavLinks>
          <NavLink to="/profile">Profil</NavLink>
          <NavLink to="/leagues">Ligaer</NavLink>
          <NavLink to="/stats">Statistikker</NavLink>
          <NavLink to="/play">Spil</NavLink>
          <LogoutButton onClick={handleLogout}>Log ud</LogoutButton>
        </NavLinks>
      </Navbar>

      <Main>
        <WelcomeSection>
          <WelcomeTitle>Velkommen tilbage, {username}!</WelcomeTitle>
          <WelcomeSubtitle>Klar til at dominere denne sæson?</WelcomeSubtitle>
        </WelcomeSection>

        <TodaysMatchesTable />

        <DashboardGrid>
          <DashboardCard>
            <CardIcon>⚽</CardIcon>
            <CardTitle>Dit Hold</CardTitle>
            <CardDescription>
              Se og administrer dit nuværende fantasy hold. Tjek spillerstatistikker og optimer din opstilling.
            </CardDescription>
            <CardButton onClick={() => navigate("/myteam")}>Administrer Hold</CardButton>
          </DashboardCard>

          <DashboardCard>
            <CardIcon>🏆</CardIcon>
            <CardTitle>Ligaer</CardTitle>
            <CardDescription>
              Deltag i spændende ligaer, se tabeller og kæmp om førstepladsen mod dine venner.
            </CardDescription>
            <CardButton onClick={() => navigate("/leagues")}>Se Ligaer</CardButton>
          </DashboardCard>

          <DashboardCard>
            <CardIcon>📊</CardIcon>
            <CardTitle>Statistikker</CardTitle>
            <CardDescription>
              Dyk ned i detaljerede statistikker og analyser for at træffe de bedste beslutninger.
            </CardDescription>
            <CardButton onClick={() => navigate("/stats")}>Udforsk Stats</CardButton>
          </DashboardCard>

          <DashboardCard>
            <CardIcon>🔄</CardIcon>
            <CardTitle>Transfers</CardTitle>
            <CardDescription>Køb og sælg spillere på transfermarkedet. Find de næste store talenter.</CardDescription>
            <CardButton onClick={() => navigate("/transfers")}>Transfer Marked</CardButton>
          </DashboardCard>
        </DashboardGrid>

        <QuickActionsSection>
          <QuickActionsTitle>Hurtige Handlinger</QuickActionsTitle>
          <ButtonsContainer>
            <ActionButton onClick={() => navigate("/play")}>🎮 Spil Nu</ActionButton>
            <ActionButton onClick={() => navigate("/myteam")}>👥 Se Dit Hold</ActionButton>
          </ButtonsContainer>
        </QuickActionsSection>
      </Main>
    </Container>
  )
}

export default DashboardPage
