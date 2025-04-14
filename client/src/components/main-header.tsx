import styles from "./main-header.module.scss";
import {Link} from "react-router";
import useUserStore from "../stores/user-store.ts";
import HeaderUser from "./header-user.tsx";
import logo from "../assets/logo.png";

function MainHeader() {
    const userStore = useUserStore();

    return (
        <div className={styles.mainHeader}>
            <Link to="/">
                <div className={styles.logo}>
                    <img className={styles.logo} src={logo} alt="logo"/>
                </div>
            </Link>
            <div className={styles.headerInfo}>
                <div className={styles.headerControls}>
                    {userStore.isLoggedIn() && <HeaderUser/>}
                </div>
            </div>
        </div>
    );
}

export default MainHeader;