import styles from "./header-user.module.scss";
import userIcon from "../assets/user_icon.svg";
import {useNavigate} from "react-router";
import useUserStore from "../stores/user-store.ts";

export default function HeaderUser() {
    const userStore = useUserStore();
    const navigate = useNavigate();

    function logout() {
        userStore.logout();
        navigate("/login");
    }

    return (
        <div className={styles.headerUser}>
            <div className={styles.avatarContainer}>
                <div>{userStore.firstName}</div>
                <img className={styles.avatar} src={userIcon} alt="user avatar"></img>
            </div>

            <div className={styles.dropdownContainer}>
                <ul>
                    <li>
                        <button onClick={logout}>Logout</button>
                    </li>
                </ul>
            </div>
        </div>
    )
}