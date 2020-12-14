import { combineReducers } from 'redux';
import { AppRootState, app } from './app';

export interface RootState {
    app: AppRootState,
}

export default combineReducers({
    app,
});
