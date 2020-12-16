import { combineReducers } from 'redux';
import { AppRootState, app } from './app';
import { TasksRootState, tasks } from './tasks';

export interface RootState {
    app: AppRootState,
    tasks:TasksRootState,
}

export default combineReducers({
    app,
    tasks,
});
