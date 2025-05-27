"use client"
import { useState } from "react"
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

const float = keyframes`
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-5px);
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
  width: 100%;
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

const LogoutButton = styled.button`
  background: linear-gradient(135deg, #e74c3c, #c0392b);
  border: none;
  padding: 0.6rem 1.2rem;
  border-radius: 25px;
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(231, 76, 60, 0.3);
  position: relative;
  overflow: hidden;
  animation: ${slideInRight} 0.8s ease-out;

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
    box-shadow: 0 6px 20px rgba(231, 76, 60, 0.4);
    
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

const Content = styled.main`
  flex: 1;
  padding: 3rem 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  z-index: 1;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;

  @media (max-width: 768px) {
    padding: 2rem 1rem;
  }
`

const ProfileHeader = styled.div`
  text-align: center;
  margin-bottom: 3rem;
  animation: ${fadeInUp} 1s ease-out;
`

const ProfileAvatar = styled.div`
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: linear-gradient(135deg, #1f7a1f, #2db82d);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 3rem;
  margin: 0 auto 1.5rem;
  box-shadow: 0 10px 30px rgba(31, 122, 31, 0.3);
  animation: ${float} 3s ease-in-out infinite;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    inset: -3px;
    border-radius: 50%;
    background: linear-gradient(135deg, #a8d5a8, #2db82d);
    z-index: -1;
    animation: ${pulse} 2s ease-in-out infinite;
  }
`

const ProfileTitle = styled.h2`
  font-size: 2.5rem;
  font-weight: 900;
  margin: 0 0 0.5rem 0;
  background: linear-gradient(135deg, #ffffff, #a8d5a8);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  line-height: 1.2;

  @media (max-width: 768px) {
    font-size: 2rem;
  }
`

const ProfileSubtitle = styled.p`
  font-size: 1.2rem;
  color: #c8e6c8;
  margin: 0;
  font-weight: 300;
`

const ProfileGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  width: 100%;
  animation: ${fadeInUp} 1s ease-out 0.3s both;

  @media (max-width: 768px) {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
`

const ProfileCard = styled.div`
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
    }
  }
`

const CardIcon = styled.div`
  font-size: 2.5rem;
  margin-bottom: 1rem;
  text-align: center;
`

const CardTitle = styled.h3`
  font-size: 1.3rem;
  font-weight: 700;
  margin: 0 0 1rem 0;
  color: #a8d5a8;
  text-align: center;
`

const StatItem = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 0;
  border-bottom: 1px solid rgba(168, 213, 168, 0.1);

  &:last-child {
    border-bottom: none;
  }
`

const StatLabel = styled.span`
  color: #c8e6c8;
  font-weight: 500;
`

const StatValue = styled.span`
  color: #ffffff;
  font-weight: 700;
  font-size: 1.1rem;
`

const EditButton = styled.button`
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
  margin-top: 1rem;
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

const AchievementBadge = styled.div`
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: linear-gradient(135deg, #ffd700, #ffed4e);
  color: #1a1a1a;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-weight: 600;
  font-size: 0.9rem;
  margin: 0.25rem;
  box-shadow: 0 2px 10px rgba(255, 215, 0, 0.3);
`

const AchievementsGrid = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  justify-content: center;
  margin-top: 1rem;
`

interface User {
  username: string
  email?: string
  joinDate?: string
  totalPoints?: number
  gamesPlayed?: number
  wins?: number
  rank?: number
}

interface ProfilePageProps {
  user?: User
  onLogout?: () => void
}

function ProfilePage({
  user = {
    username: "Spiller",
    email: "spiller@example.com",
    joinDate: "Januar 2024",
    totalPoints: 1250,
    gamesPlayed: 15,
    wins: 8,
    rank: 42,
  },
  onLogout,
}: ProfilePageProps) {
  const [isEditing, setIsEditing] = useState(false)

  const achievements = [
    { icon: "🏆", name: "Første Sejr" },
    { icon: "⚽", name: "Målscorer" },
    { icon: "🎯", name: "Præcision" },
    { icon: "🔥", name: "Vinderstribe" },
  ]

  return (
    <Container>
      <Navbar>
        <Logo>FantasyFootball</Logo>
        <LogoutButton onClick={onLogout}>Log ud</LogoutButton>
      </Navbar>

      <Content>
        <ProfileHeader>
          <ProfileAvatar>👤</ProfileAvatar>
          <ProfileTitle>Velkommen, {user.username}!</ProfileTitle>
          <ProfileSubtitle>Fantasy Football Mester</ProfileSubtitle>
        </ProfileHeader>

        <ProfileGrid>
          <ProfileCard>
            <CardIcon>📊</CardIcon>
            <CardTitle>Statistikker</CardTitle>
            <StatItem>
              <StatLabel>Samlede Point</StatLabel>
              <StatValue>{user.totalPoints?.toLocaleString()}</StatValue>
            </StatItem>
            <StatItem>
              <StatLabel>Spil Spillet</StatLabel>
              <StatValue>{user.gamesPlayed}</StatValue>
            </StatItem>
            <StatItem>
              <StatLabel>Sejre</StatLabel>
              <StatValue>{user.wins}</StatValue>
            </StatItem>
            <StatItem>
              <StatLabel>Nuværende Rang</StatLabel>
              <StatValue>#{user.rank}</StatValue>
            </StatItem>
          </ProfileCard>

          <ProfileCard>
            <CardIcon>👤</CardIcon>
            <CardTitle>Brugeroplysninger</CardTitle>
            <StatItem>
              <StatLabel>Brugernavn</StatLabel>
              <StatValue>{user.username}</StatValue>
            </StatItem>
            <StatItem>
              <StatLabel>Email</StatLabel>
              <StatValue>{user.email}</StatValue>
            </StatItem>
            <StatItem>
              <StatLabel>Medlem siden</StatLabel>
              <StatValue>{user.joinDate}</StatValue>
            </StatItem>
            <StatItem>
              <StatLabel>Status</StatLabel>
              <StatValue>Aktiv</StatValue>
            </StatItem>
            <EditButton onClick={() => setIsEditing(!isEditing)}>
              {isEditing ? "Gem Ændringer" : "Rediger Profil"}
            </EditButton>
          </ProfileCard>

          <ProfileCard>
            <CardIcon>🏅</CardIcon>
            <CardTitle>Præstationer</CardTitle>
            <AchievementsGrid>
              {achievements.map((achievement, index) => (
                <AchievementBadge key={index}>
                  <span>{achievement.icon}</span>
                  <span>{achievement.name}</span>
                </AchievementBadge>
              ))}
            </AchievementsGrid>
            <EditButton onClick={() => alert("Se alle præstationer!")}>Se Alle Præstationer</EditButton>
          </ProfileCard>

          <ProfileCard>
            <CardIcon>⚙️</CardIcon>
            <CardTitle>Indstillinger</CardTitle>
            <StatItem>
              <StatLabel>Notifikationer</StatLabel>
              <StatValue>Aktiveret</StatValue>
            </StatItem>
            <StatItem>
              <StatLabel>Privatliv</StatLabel>
              <StatValue>Offentlig</StatValue>
            </StatItem>
            <StatItem>
              <StatLabel>Sprog</StatLabel>
              <StatValue>Dansk</StatValue>
            </StatItem>
            <StatItem>
              <StatLabel>Tema</StatLabel>
              <StatValue>Mørk</StatValue>
            </StatItem>
            <EditButton onClick={() => alert("Åbn indstillinger!")}>Administrer Indstillinger</EditButton>
          </ProfileCard>
        </ProfileGrid>
      </Content>
    </Container>
  )
}

export default ProfilePage
