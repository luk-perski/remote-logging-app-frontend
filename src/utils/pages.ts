import { AddTask } from "../pages/AddTask";
import { Login } from "../pages/Login";
import { Projects } from "../pages/Projects";
import { TaskDetails } from "../pages/TaskDetails";
import { Tasks } from "../pages/Tasks";

export const pages = {
    tasks: {
        Component: Tasks,
        url: () => '/tasks'
    },
    taskDetails: {
        Component: TaskDetails,
        url: (id?: string) => id ? `/tasks/${id}` : '/tasks/:taskId'
    },
    addTask: {
        Component: AddTask,
        url: () => '/tasks/new'
    },
    projects: {
        Component: Projects,
        url: () => '/projects'
    },
    login: {
        Component: Login,
        url: () => '/login'
    }
}