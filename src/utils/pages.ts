import { Login } from "../pages/Login";
import { Tasks } from "../pages/Tasks";

export const pages = {
    tasks : {
        Component: Tasks,
        url: ()=> '/tasks'
    },
    login: {
        Component: Login,
        url: ()=> '/login'
    }
}