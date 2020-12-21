import { combineReducers } from 'redux';
import { AppRootState, app } from './app';
import { LoginRootState, login} from './login';
import { TasksRootState, tasks } from './tasks';

export interface RootState {
    app: AppRootState,
    tasks:TasksRootState,
    login: LoginRootState
}

export default combineReducers({
    app,
    tasks,
    login
});
