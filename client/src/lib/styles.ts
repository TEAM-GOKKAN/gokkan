import { createGlobalStyle } from 'styled-components';
import reset from 'styled-reset';

const GlobalStyle = createGlobalStyle`
  ${reset}

  @import url('https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap');
  @import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap');
  
  @font-face {
    font-family: 'Poppins', sans-serif;
    src: url('https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap')
      format("woff");
    font-weight: normal;
    font-style: normal;
    unicode-range: U+0041-005A, U+0061-007A;
  }

  @font-face {
    font-family: 'Noto Sans KR', sans-serif;
    src: url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap')
      format("woff");
    font-weight: normal;
    font-style: normal;
    unicode-range: U+AC00-U+D7A3;
  }

  :root {
    /* Color */
    --color-brown100: #EEEDEC;
    --color-brown200: #D9D6D4;
    --color-brown300: #9D9792;
    --color-brown400: #686059;
    --color-brown500: #4B433D;
    --color-purple: #808AD2;
    --color-orange: #E05326;
    --color-white: #ffffff;

    /* Font Size */
    --font-x-micro: 10px;
    --font-micro: 12px;
    --font-small: 14px;
    --font-regular: 15px;
    --font-medium: 16px;
    --font-large: 18px;
    --font-x-large: 20px;
    --font-2x-large: 22px;
    --font-huge: 24px;

    /* Font Weight */
    --weight-thin: 100;
    --weight-light: 300;
    --weight-regular: 400;
    --weight-semi-bold: 500;
    --weight-bold: 700;

    /* Border Radius */
    --size-border-radius-small: 4px;
    --size-border-radius-large: 8px;

    /* Animation */
    --animation-duration: 300ms;
  }

  *, *::before, *::after {
    box-sizing: border-box;
    letter-spacing: -0.0625em;
  }

  *:focus {
    outline: 0;
  }

  /* html {
    height: 100%;
  } */

  body {
    white-space: nowrap;
    color: var(--color-brown500);
    background: var(--color-white);
    font-family: 'Poppins', 'Noto Sans KR', sans-serif;
    font-size: var(--font-regular);
    letter-spacing: -0.0625em;
    /* overflow-x: hidden; */
  }

  a {
    text-decoration: none;
    color: inherit;
  }

  button {
    cursor: pointer;
    color: var(--color-brown300);
    border: 0;
    padding: 0;
    margin: 0;
    background: none;
  }

  h1, h2, h3, h4, h5, h6 {
    color: var(--color-brown500);
  }

  input {
    height: 42px;
    color: var(--color-brown500);
    border: none;
    background-color: var(--color-brown100);

    &::placeholder {
      color: var(--color-brown300);
    }
  }
  
  textarea {
    font-family: 'Poppins', 'Noto Sans KR', sans-serif;
    color: var(--color-brown500);
    background: var(--color-brown100);
    
    &::placeholder {
      color: var(--color-brown300);
    }
  }
`;

export default GlobalStyle;
