export interface CategoryRootState {
    categoryList: JsonSchema.ModelApiCategory[] | null;
}

export const categories = (state: CategoryRootState = {
    categoryList: null,
}, action: Record<string, any>) => {
    switch (action.type) {
        case 'SET_CATEGORIES':
            return { ...state, categoryList: action.categories };
        default:
            return state;
    }
}