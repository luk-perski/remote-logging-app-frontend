import { Dispatch } from 'redux';
import * as categoryApi from '../../api/category';


export const getCategories = () => {
    return async (dispatch: Dispatch) => {
        const categories = await categoryApi.getAll();
        console.log(categories)

        dispatch(setCategories(categories));
    }
}

export const setCategories = (categories: JsonSchema.ModelApiCategory[]) => ({
    type: 'SET_CATEGORIES',
    categories,
});