import { Outlet } from 'react-router-dom';
import styled from 'styled-components';
import Header from '../components/Header/Header';

export default function LayoutPage() {
  return (
    <Container>
      <Header />
      <Main>
        <Outlet />
      </Main>
    </Container>
  );
}

const Container = styled.div`
  width: 100%;
  overflow-x: hidden;
`;

const Main = styled.main`
  padding: 0 16px;
  margin-top: 60px;
  padding-top: 32px;
`;
