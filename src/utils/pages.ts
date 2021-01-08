import { Login } from "../pages/Login";
import { TaskDetails } from "../pages/TaskDetails";
import { Tasks } from "../pages/Tasks";

export const pages = {
    tasks : {
        Component: Tasks,
        url: ()=> '/tasks'
    },
    taskDetails : {
        Component: TaskDetails,
        url: (id?: string)=> id? `/tasks/${id}` : '/tasks/:taskId' 
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