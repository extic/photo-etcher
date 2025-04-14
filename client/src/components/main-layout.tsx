import {Layout} from 'antd';
import MainHeader from "./main-header.tsx";
import {useEffect} from "react";
import useUserStore from "../stores/user-store.ts";
import {Outlet, useNavigate} from "react-router";

function MainLayout() {
    const { Header, Content } = Layout;
    const userStore = useUserStore();
    const navigate = useNavigate();

    useEffect(() => {
        if (!userStore.token && location.pathname !== "/login") {
            navigate("/login");
        }
    })

    return (
        <Layout style={{ minHeight: '100vh' }}>
            <Header style={{ padding: 0, background: '#edebe8', boxShadow: '0 3px 21px -5px #cdd1e1', zIndex: 1 }}>
                <MainHeader/>
            </Header>
            <Content style={{ margin: '0 16px' }}>
                <Outlet/>
            </Content>
        </Layout>
    )
}

export default MainLayout;