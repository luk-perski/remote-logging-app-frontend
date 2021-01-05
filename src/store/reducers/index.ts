import { combineReducers } from 'redux';
import { AppRootState, app } from './app';
import { LoginRootState, login} from './login';
import { ProjectRootState, projects } from './projects';
import { TasksRootState, tasks } from './tasks';

export interface RootState {
    app: AppRootState,
    tasks:TasksRootState,
    login: LoginRootState,
    projects: ProjectRootState
}

export default combineReducers({
    app,
    tasks,
    login,
    projects
});
