import { Login } from "../pages/Login";
import { Tasks } from "../pages/Tasks";

export const pages = {
    tasks : {
        Component: Tasks,
        url: ()=> '/tasks'
    },    
    projects : {
        Component: Tasks,
        url: ()=> '/projects'
    },
    login: {
        Component: Login,
        url: ()=> '/login'
    }
}