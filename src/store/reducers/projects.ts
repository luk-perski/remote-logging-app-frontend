export interface ProjectRootState {
    loadingProjects: boolean,
    projectsList: JsonSchema.ModelApiProject[] | null,
}

export const projects = (state: ProjectRootState = {
    loadingProjects: false,
    projectsList: null,
}, action: Record<string, any>) => { 
    switch(action.type){
        case 'LOADING_PROJECTS':
            return {...state, loadingProjects: true};
        case 'SET_PROJECTS':
            return{state, projectsList: action.projects, loadingProjects: false};
        default:
            return state;
    }
}
