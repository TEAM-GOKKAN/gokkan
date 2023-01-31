import CategoryNav from '../components/CategorySearch/CategoryNav';
import CategorySearchMain from '../components/CategorySearch/CategorySearchMain';
import styled from 'styled-components';

const CategorySearchPage = () => {
  return (
    <Container>
      <CategoryNav />
      <CategorySearchMain />
    </Container>
  );
};

const Container = styled.div`
  overflow: hidden;
`;

export default CategorySearchPage;
