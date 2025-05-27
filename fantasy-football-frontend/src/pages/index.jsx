import { Link } from "react-router-dom"
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

const float = keyframes`
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-10px);
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

const LoginLink = styled(Link)`
  color: #ffffff;
  background: linear-gradient(135deg, #1f7a1f, #2db82d);
  padding: 0.75rem 1.5rem;
  border-radius: 50px;
  font-weight: 600;
  text-decoration: none;
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
    box-shadow: 0 6px 25px rgba(31, 122, 31, 0.4);
    
    &::before {
      left: 100%;
    }
  }

  &:active {
    transform: translateY(0);
  }

  @media (max-width: 768px) {
    padding: 0.6rem 1.2rem;
    font-size: 0.9rem;
  }
`

const Main = styled.main`
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding: 4rem 2rem;
  width: 100%;
  max-width: 1000px;
  margin: 0 auto;
  position: relative;
  z-index: 1;

  @media (max-width: 768px) {
    padding: 2rem 1rem;
  }
`

const HeroTitle = styled.h2`
  font-size: 3.5rem;
  font-weight: 900;
  margin: 0 0 1.5rem 0;
  background: linear-gradient(135deg, #ffffff, #a8d5a8);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  animation: ${fadeInUp} 1s ease-out;
  line-height: 1.2;
  letter-spacing: -1px;

  @media (max-width: 768px) {
    font-size: 2.5rem;
  }

  @media (max-width: 480px) {
    font-size: 2rem;
  }
`

const HeroSubtitle = styled.p`
  font-size: 1.3rem;
  line-height: 1.6;
  margin: 0 0 2.5rem 0;
  color: #c8e6c8;
  max-width: 600px;
  animation: ${fadeInUp} 1s ease-out 0.2s both;
  font-weight: 300;

  @media (max-width: 768px) {
    font-size: 1.1rem;
    margin: 0 0 2rem 0;
  }
`

const CTAButton = styled.button`
  background: linear-gradient(135deg, #1f7a1f, #2db82d);
  border: none;
  border-radius: 50px;
  color: white;
  padding: 1rem 2.5rem;
  font-size: 1.2rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 6px 25px rgba(31, 122, 31, 0.3);
  animation: ${fadeInUp} 1s ease-out 0.4s both, ${float} 3s ease-in-out infinite;
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
    transform: translateY(-3px) scale(1.05);
    box-shadow: 0 8px 30px rgba(31, 122, 31, 0.4);
    
    &::before {
      left: 100%;
    }
  }

  &:active {
    transform: translateY(-1px) scale(1.02);
  }

  @media (max-width: 768px) {
    padding: 0.8rem 2rem;
    font-size: 1.1rem;
  }
`

const FeatureGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  margin-top: 4rem;
  width: 100%;
  animation: ${fadeInUp} 1s ease-out 0.6s both;

  @media (max-width: 768px) {
    margin-top: 3rem;
    gap: 1.5rem;
  }
`

const FeatureCard = styled.div`
  background: rgba(18, 44, 18, 0.6);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(168, 213, 168, 0.2);
  border-radius: 20px;
  padding: 2rem;
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
    border-color: rgba(168, 213, 168, 0.4);
  }
`

const FeatureIcon = styled.div`
  font-size: 2.5rem;
  margin-bottom: 1rem;
`

const FeatureTitle = styled.h3`
  font-size: 1.3rem;
  font-weight: 700;
  margin: 0 0 0.5rem 0;
  color: #a8d5a8;
`

const FeatureDescription = styled.p`
  font-size: 1rem;
  color: #c8e6c8;
  margin: 0;
  line-height: 1.5;
`

function IndexPage() {
  return (
    <Container>
      <Navbar>
        <Logo>FantasyFootball</Logo>
        <LoginLink to="/login">Log ind</LoginLink>
      </Navbar>

      <Main>
        <HeroTitle>Velkommen til FantasyFootball!</HeroTitle>
        <HeroSubtitle>
          Oplev den ultimative fantasy football oplevelse med avancerede statistikker, realtidsdata og
          konkurrencedygtige ligaer. Byg dit drømmehold og dominér sæsonen!
        </HeroSubtitle>
        <CTAButton onClick={() => alert("Kom i gang med din fantasy rejse!")}>Start Din Rejse</CTAButton>

        <FeatureGrid>
          <FeatureCard>
            <FeatureIcon>⚽</FeatureIcon>
            <FeatureTitle>Realtids Statistikker</FeatureTitle>
            <FeatureDescription>Få øjeblikkelige opdateringer og detaljerede spillerstatistikker</FeatureDescription>
          </FeatureCard>

          <FeatureCard>
            <FeatureIcon>🏆</FeatureIcon>
            <FeatureTitle>Konkurrencedygtige Ligaer</FeatureTitle>
            <FeatureDescription>Deltag i spændende ligaer og kæmp om førstepladsen</FeatureDescription>
          </FeatureCard>

          <FeatureCard>
            <FeatureIcon>📊</FeatureIcon>
            <FeatureTitle>Avanceret Analyse</FeatureTitle>
            <FeatureDescription>Træf smarte beslutninger med dybdegående spilleranalyser</FeatureDescription>
          </FeatureCard>
        </FeatureGrid>
      </Main>
    </Container>
  )
}

export default IndexPage
