import {ConfigProvider} from "antd";
import {BrowserRouter, Navigate, Route, Routes} from "react-router";
import MainLayout from "./components/main-layout.tsx";
import LoginPage from "./pages/login/login-page.tsx";
import ProcessingPage from "./pages/processing-page.tsx";

function App() {

    return (
        <ConfigProvider theme={{cssVar: true}}>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Navigate to={"/main"}/>}/>

                    <Route path="login" element={<MainLayout/>}>
                        <Route index element={<LoginPage/>}/>
                    </Route>

                    <Route path="main" element={<MainLayout/>}>
                        <Route index element={<ProcessingPage/>}/>
                    </Route>
                </Routes>
            </BrowserRouter>
        </ConfigProvider>
    )
}

export default App
