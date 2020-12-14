import cookie from 'js-cookie';
import { apiInstance } from '../../api';
import { languageStringsByLanguage } from '../../language-strings';

export interface AppRootState {
    loadingSessionUser: boolean,
    loadedSessionUser: boolean,
    sessionUser: any,
    language: string,
    environment: string | undefined,
    token: string | undefined,
    languageStrings: typeof languageStringsByLanguage.en | typeof languageStringsByLanguage.pt,
    oneDriveStatus: string | null,
}

function processCookie(cookie: string | undefined) {
    if(cookie && cookie !== 'undefined' && cookie !== 'null') {
        return cookie;
    }

    return undefined;
}

function validLanguage(lang: any) {
    return Object.keys(languageStringsByLanguage).includes(lang);
}

const token = processCookie(cookie.get('token'));

let language = processCookie(cookie.get('language')) as 'pt' | 'en';
if(!validLanguage(language)) {
    language = 'en';
}
const languageStrings = languageStringsByLanguage[language];

if(token) {
    apiInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

export const app = (state: AppRootState = {
    loadingSessionUser: false,
    loadedSessionUser: false,
    sessionUser: null,
    language,
    environment: process.env.REACT_APP_HOST_TYPE,
    token,
    languageStrings,
    oneDriveStatus: null,
}, action: Record<string, any>)=> {
    switch(action.type) {
        case 'SET_LOADING_SESSION_USER':
            return { ...state, loadingSessionUser: true };
        case 'SET_SESSION_USER':
            return { ...state, sessionUser: action.user, loadingSessionUser: false, loadedSessionUser: true };
        case 'REMOVE_SESSION_USER':
            return {...state, sessionUser: null };
        case 'CHANGING_SESSION_USER_ROLE':
            return { ...state, changingSessionUserRole: true };
        case 'UPDATE_SESSION_USER':
            return { ...state, changingSessionUserRole: false, sessionUser: action.user };
        case 'CHANGE_LANGUAGE':
            let language = action.language as 'en' | 'pt';
            if(!validLanguage(language)) {
                language = 'en';
            }
            cookie.set('language', language, { sameSite: 'strict' });

            return { ...state, language: language, languageStrings: languageStringsByLanguage[language] };
        case 'SET_TOKEN':            
            if(processCookie(action.token)) {
                cookie.set('token', action.token, { sameSite: 'strict' });

                apiInstance.defaults.headers.common['Authorization'] = `Bearer ${action.token}`;
            } else {
                cookie.remove('token');
                apiInstance.defaults.headers.common['Authorization'] = undefined;

                action.token = undefined;
            }

            return { ...state, token: action.token };
        case 'SET_ONE_DRIVE_STATUS':
            return {
                ...state,
                oneDriveStatus: action.status,
            }
        default:
            return state;
    }
}
