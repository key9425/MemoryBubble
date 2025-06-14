import { Routes, Route, BrowserRouter, Navigate } from 'react-router-dom';
import { useEffect } from 'react';
import useUser from './hooks/useUser';
import {
  requestNotificationPermission,
  onMessageListener,
} from './hooks/firebase';

// 보호된 라우트 컴포넌트들
import {
  ProtectedRoute,
  FamilyCreationRoute,
  ProfileCreationRoute,
  PublicRoute,
  OAuthRoute,
  AdminRoute,
} from './components/ProtectedRoute';

// 페이지 컴포넌트들
import LoadingPage from './pages/LoadingPage';
import BasicPhotoAlbumPage from './pages/BasicPhotoAlbumPage';
import FontPage from './pages/FontPage';
import PhotoAlbumPage from './pages/PhotoAlbumPage';
import WriteLetterPage from './pages/WriteLetterPage';
import CalendarPage from './pages/CalendarPage';
import MainWithLoading from './pages/MainWithLoading';
import LetterStoragePage from './pages/LetterStoragePage';
import EnterPage from './pages/EnterPage';
import JoinPage from './pages/JoinPage';
import CreateGroupPage from './pages/CreateGroupPage';
import Header from './components/header/Header';

// 관리자 페이지
import AdminPage from './pages/AdminPage';
import OAuthCallback from './components/oauth/OAuthCallback';
import LandingWithIntro from './pages/LandingWithIntro';
// import PWAInstaller from './components/PWAInstaller';

type NotificationPayload = {
  notification?: {
    title?: string;
    body?: string;
  };
};

function App() {
  const { isLoading, checkAuthAndFetchUserData } = useUser();

  // 컴포넌트 마운트 시 인증 확인 및 사용자 정보 요청
  useEffect(() => {
    checkAuthAndFetchUserData();
  }, []);

  // fcm token 요청
  useEffect(() => {
    const initFCM = async (): Promise<void> => {
      // 매 새로고침마다 권한 요청
      await requestNotificationPermission();
    };

    initFCM();

    onMessageListener()
      .then((payload: NotificationPayload) => {
        if (payload.notification) {
          alert(
            `알림 도착: ${payload.notification.title} ${payload.notification.body}`
          );
        }
      })
      .catch((err) => console.log('FCM 메시지 리스너 오류:', err));
  }, []);

  if (isLoading) {
    // 로딩 중일 때 표시할 컴포넌트
    return <LoadingPage message="Now Loading..." />;
  }

  return (
    <div className="font-pretendard font-normal min-w-80">
      <BrowserRouter>
        <Header />
        <Routes>
          {/* 로그인 상태와 관계없이 접근 가능한 경로 (로그인 시 가족/프로필 정보 필요) */}
          <Route element={<PublicRoute />}>
            <Route index path="/" element={<LandingWithIntro />} />
          </Route>

          {/* 로그인하지 않은 사용자만 접근 가능한 경로 */}
          <Route element={<OAuthRoute />}>
            <Route path="/oauth/callback" element={<OAuthCallback />} />
          </Route>

          {/* 가족 생성/가입 페이지 - 인증 필요, 가족 없어야 함 */}
          <Route element={<FamilyCreationRoute />}>
            <Route path="/enter" element={<EnterPage />} />
            <Route path="/create" element={<CreateGroupPage />} />
          </Route>

          {/* 사용자 정보 등록 페이지 - 인증 필요, 가족 있어야 함, 프로필 없어야 함 */}
          <Route element={<ProfileCreationRoute />}>
            <Route path="/join" element={<JoinPage />} />
          </Route>

          {/* 완전 보호된 경로 - 모든 조건 충족 필요 */}
          <Route element={<ProtectedRoute />}>
            <Route index path="/main" element={<MainWithLoading />} />
            <Route path="/font" element={<FontPage />} />
            <Route path="/letter" element={<WriteLetterPage />} />
            <Route path="/album/basic" element={<BasicPhotoAlbumPage />} />
            <Route path="/album" element={<PhotoAlbumPage />} />
            <Route path="/album/:id" element={<PhotoAlbumPage />} />
            <Route path="/storage" element={<LetterStoragePage />} />
            <Route path="/calendar" element={<CalendarPage />} />
          </Route>

          {/* 관리자 경로 - 인증 필요, 관리자 역할 필요 */}
          <Route element={<AdminRoute />}>
            <Route path="/admin" element={<AdminPage />} />
          </Route>

          {/* 일치하는 경로가 없는 경우 랜딩 페이지로 리다이렉트 */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
