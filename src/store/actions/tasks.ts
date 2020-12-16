import { Dispatch } from 'redux';
import * as tasksApi from '../../api/tasks';

export const setTasks = (tasks: JsonSchema.ModelApiTask[])=> ({
    type: 'SET_TASKS',
    tasks,
});


export const getTasks = () => {
    return async (dispatch: Dispatch)=> {
        dispatch({
            type: 'LOADING_TASKS',
        });

        const reports = await tasksApi.getTasks();

        dispatch(setTasks(reports));
    }
};