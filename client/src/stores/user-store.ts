import {create} from 'zustand'
import {Cookies} from "react-cookie";

const cookies = new Cookies();

type UserStore = {
    firstName: string,
    lastName: string,
    roles: string[],
    token: string | null,
    login: (firstName: string, lastName: string, token: string, roles: string[]) => void;
    logout: () => void,
    isLoggedIn: () => boolean,
    hasRole: (role: string) => boolean,
}

const useUserStore = create<UserStore>()((set, get) => ({
    firstName: "",
    lastName: "",
    token: cookies.get("authToken") || null,
    roles: [],

    login: (firstName: string, lastName: string, token: string, roles: string[]) => {
        if (token) {
            // cookie expires in 1 hour
            cookies.set("authToken", token, { path: "/", expires: new Date(Date.now() + 3600_000) });
        } else {
            cookies.remove("authToken", { path: "/" });
        }
        set(() => ({ firstName, lastName, token, roles }))
    },

    logout: () => {
        cookies.remove("authToken", { path: "/" });
        set(() => ({ firstName: "", lastName: "", token: null }))
    },

    isLoggedIn: () => get().token !== null,

    hasRole: (role: string) => get().roles.includes(role)
}))

export default useUserStore;
