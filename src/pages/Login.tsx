
import React from 'react';
import queryString from 'query-string';
import { Redirect, useHistory, useLocation } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircle, faLock } from "@fortawesome/free-solid-svg-icons";
import { setToken } from "../store/actions/app";
import { languages } from '../language-strings';
import { API_URL } from '../config';
import { RootState } from '../store/reducers';

export const Login = ()=> {
    const history = useHistory();
    const location = useLocation();
    const dispatch = useDispatch();
    const state = useSelector((state: RootState) => state);
    const app = state.app;
    const params = queryString.parse(location.search);

    if(params.token) {
        const token = params.token;

        dispatch(setToken(typeof token === 'object'? token[0] : token));
        history.replace('/login');

        return <Redirect to="/" />
    }

    // const env = app.environment;
    const lang = app.language;

    return (
        <div className="mx-auto">
            <div className="w-full mt-16">
                <div className="relative m-auto">
                    <FontAwesomeIcon icon={faCircle} className="fa-stack-2x text-4xl" />
                    <FontAwesomeIcon icon={faLock} className="fa-stack-1x fa-inverse text-3xl" />
                </div>
            </div>
            <div style={{ marginTop: '120px' }} className="flex justify-center font-bold">
                <span className="m-auto text-2xl">
                    {lang === languages.ENGLISH && 'Authentication necessary'}
                    {lang === languages.PORTUGUESE && 'Autenticação Necessária'}
                </span>
            </div>
            <div className="w-88 m-auto mt-4 text-sm">
                <div className="iscte-warning-yellow bg-iscte-warning-yellow text-base border-iscte-warning-yellow p-4 mb-5">
                    {lang === languages.ENGLISH && 'To access this content you have to authenticate first.'}
                    {lang === languages.PORTUGUESE && 'Para aceder ao conteúdo desejado terá de autenticar-se primeiro.'}
                </div>
                <div className="font-bold mb-3">
                    Se tiver uma conta válida do Iscte, inicie a sessão na Google ou Microsoft com as mesmas credenciais que usa para aceder ao Fénix.
                </div>
                <div className="mb-3">
                    Se não tiver uma conta válida do Iscte, pode usar uma conta Google ou Microsoft (Office 365 ou Outlook) para aceder ao sistema mas só terá acesso aos conteúdos que estão disponíveis para o público.
                </div>
                <div className="mb-3">
                    Clique num dos botões em baixo para iniciar a sessão.
                </div>
                <div>
                    <a href={`${API_URL}/login/google`}>
                        <img src="/images/sign_in_google.png" alt="google_login" />
                    </a>
                    <a href={`${API_URL}/login/office365`}>
                        <img src="/images/sign_in_microsoft.png" alt="google_login" />
                    </a>
                </div>
            </div>
        </div>
    )
}
