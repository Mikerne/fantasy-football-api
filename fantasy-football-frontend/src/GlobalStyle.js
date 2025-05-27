// GlobalStyle.js
import { createGlobalStyle } from 'styled-components';

const GlobalStyle = createGlobalStyle`
  *, *::before, *::after {
    box-sizing: border-box;
  }

  body {
    margin: 0;
    min-height: 100vh;
    font-family: system-ui, Avenir, Helvetica, Arial, sans-serif;
    background-color: #0b3d0b;  /* Din grønne baggrund */
    color: #d4f1be;              /* Din lyse tekstfarve */
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }

  a {
    color: #a8d5a8;
    text-decoration: none;
    font-weight: 500;

    &:hover {
      color: #2db82d;
    }
  }

  button {
    border-radius: 8px;
    border: none;
    padding: 0.6em 1.2em;
    font-size: 1em;
    font-weight: 500;
    cursor: pointer;
    background-color: #1f7a1f;
    color: white;
    transition: background-color 0.25s;

    &:hover {
      background-color: #2db82d;
    }

    &:focus {
      outline: 3px solid #2db82d;
      outline-offset: 2px;
    }
  }
`;

export default GlobalStyle;
