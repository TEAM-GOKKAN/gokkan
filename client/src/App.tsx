import { Suspense, lazy } from 'react';
import { Route, Routes, useLocation } from 'react-router-dom';
import LoadingSpinner from './components/common/LoadingSpinner';
import GlobalStyle from './lib/styles/global';

const LayoutPage = lazy(() => import('./pages/LayoutPage'));
const MyPage = lazy(() => import('./pages/MyPage'));
const MainPage = lazy(() => import('./pages/MainPage'));
const LotDetailPage = lazy(() => import('./pages/LotDetailPage'));
const BidModal = lazy(() => import('./components/LotDetail/Bid/BidModal'));
const AuctionRegisterPage = lazy(() => import('./pages/AuctionRegisterPage'));
const ExpertWorkDetailPage = lazy(() => import('./pages/ExpertWorkDetailPage'));
const ExpertWorkListPage = lazy(() => import('./pages/ExpertWorkListPage'));
const SignUpPage = lazy(() => import('./pages/SignUpPage'));
const NotFoundPage = lazy(() => import('./pages/NotFoundPage'));
const SignInPage = lazy(() => import('./pages/SignInPage'));
const SignInCheck = lazy(() => import('./components/SignIn/SignInCheck'));
const MyWritingProductPage = lazy(() => import('./pages/MyWritingProductPage'));
const PaymentPage = lazy(() => import('./pages/PaymentPage'));
const PaymentResultPage = lazy(() => import('./pages/PaymentResultPage'));
const ExaminePage = lazy(() => import('./pages/ExaminePage'));
const CategoryModalPage = lazy(() => import('./pages/CategoryModalPage'));
const CategorySearchPage = lazy(() => import('./pages/CategorySearchPage'));
const FilterPage = lazy(() => import('./pages/FilterPage'));
const MyRegisteredLotPage = lazy(() => import('./pages/MyRegisteredLotPage'));
const MyReturnedLotPage = lazy(() => import('./pages/MyReturnedLotPage'));
const MyBidLotPage = lazy(() => import('./pages/MyBidLotPage'));
const MySuccesfulBidLotPage = lazy(
  () => import('./pages/MySuccessfulBidLotPage')
);
const MyFinishedBidLotPage = lazy(() => import('./pages/MyFinishedBidLotPage'));

function App() {
  const location = useLocation();
  const background = location.state?.background;

  return (
    <Suspense fallback={<LoadingSpinner />}>
      <GlobalStyle />
      <Routes location={background || location}>
        <Route path="/" element={<LayoutPage />}>
          <Route path="/" element={<MainPage />} />
          <Route
            path="auction/:itemId/:auctionId"
            element={<LotDetailPage />}
          />
          <Route path="signup" element={<SignUpPage />} />
          <Route path="signInCheck" element={<SignInCheck />} />
          <Route path="myWritingProduct" element={<MyWritingProductPage />} />
          <Route path="myRegisteredLot" element={<MyRegisteredLotPage />} />
          <Route path="myReturnedLot" element={<MyReturnedLotPage />} />
          <Route path="myBidLot" element={<MyBidLotPage />} />
          <Route
            path="mySuccessfulBidLot"
            element={<MySuccesfulBidLotPage />}
          />
          <Route path="myFinishedBidLot" element={<MyFinishedBidLotPage />} />
          <Route path="expertWorkList" element={<ExpertWorkListPage />} />
          <Route
            path="categorySearch/:category"
            element={<CategorySearchPage />}
          />
          <Route
            path="expertWorkDetail/:itemId"
            element={<ExpertWorkDetailPage />}
          />
          <Route path="payment/:itemId/:auctionId" element={<PaymentPage />} />
          <Route
            path="payment/:itemId/:auctionId/result"
            element={<PaymentResultPage />}
          />
        </Route>
        <Route
          path="/register/:pageNumber/:productId"
          element={<AuctionRegisterPage />}
        />
      </Routes>
      {background && (
        <Routes>
          <Route
            path="/auction/:itemId/:auctionId/bid"
            element={<BidModal />}
          />
          <Route path="/signin" element={<SignInPage />} />
          <Route path="/myPage" element={<MyPage />} />
          <Route path="/categoryModal" element={<CategoryModalPage />} />
          <Route path="/filter" element={<FilterPage />} />
          <Route path="/examine/:itemId" element={<ExaminePage />} />
        </Routes>
      )}
    </Suspense>
  );
}

export default App;
