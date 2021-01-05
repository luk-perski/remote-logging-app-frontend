import { Dispatch } from 'redux';
import * as projectsApi from '../../api/projects';

export const setProjects = (projects: JsonSchema.ModelApiProject[])=> ({
    type: 'SET_PROJECTS',
    projects,
}); 


export const getProjects = () => {
    return async (dispatch: Dispatch)=> {
        dispatch({
            type: 'LOADING_PROJECTS',
        });

        const projects = await projectsApi.getProjects();

        dispatch(setProjects(projects));
    }
};