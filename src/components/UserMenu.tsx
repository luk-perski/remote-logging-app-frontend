import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { Link, useLocation } from 'react-router-dom';
import { faCaretDown, faCaretUp, IconDefinition } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { userRoleFieldByLanguage } from '../store/actions/app';
import { RootState } from '../store/reducers';
import { menuSchema } from '../utils/menuSchema';
import { API_URL } from '../config';

interface UserMenuOptions {
    href: string;
    icon: IconDefinition;
    selected: boolean;
    label: string;
}

const UserMenuTab = ({ title, options }: { title: string, options: UserMenuOptions[] })=> {
    const [isOpen, setIsOpen] = useState(true);

    return (
        <div className="bg-iscte-gray mt-4 pt-1 pr-2">
            <div
                onClick={()=> setIsOpen(!isOpen)}
                className="flex iscte-blue justify-between p-2 pl-6 cursor-pointer"
            >
                <div className="font-bold uppercase text-sm">
                    {title}
                </div>
                <div>
                    <FontAwesomeIcon icon={isOpen? faCaretUp : faCaretDown} />
                </div>
            </div>
            {isOpen && (
                <div className="text-sm pb-2">
                    {options.map((option: UserMenuOptions)=> (
                        <Link key={option.href} to={option.href} className={`flex py-1 pl-6 cursor-pointer ${option.selected? 'bg-iscte-blue text-white' : ''}`}>
                            <div className="w-6">
                                <FontAwesomeIcon icon={option.icon} className="mr-2" />
                            </div>
                            <div className="hover:underline">
                                {option.label}
                            </div>
                        </Link>
                    ))}
                </div>
            )}
        </div>
    )
}

export const UserMenu = ()=> {
    const state = useSelector((state: RootState) => state);
    const app = state.app;
    const sessionUser = app.sessionUser;
    const location = useLocation();
    const lang = app.language;
    const roleField = userRoleFieldByLanguage[lang];
    const pathname = location.pathname;
    const languageStrings = app.languageStrings;

    return (
        <div className="w-88 h-full bg-iscte-gray-light mr-8 flex-shrink-0">
            <div className="py-5 bg-iscte-gray text-center">
                <img className="rounded-full w-24 mx-auto" src={`${API_URL}${sessionUser.picture}` || '/images/default_sessionUser.png'} alt="usr" />
                <div className="text-sm mt-2">
                    {sessionUser.first_name}{' '}{sessionUser.last_name}
                </div>
                <div className="text-xs">
                    {sessionUser.active_role? (
                        <span className="capitalize">
                            {sessionUser.active_role[roleField]}
                        </span>
                    ) : (
                        <>
                            {languageStrings.iscte_user}
                        </>
                    )}
                </div>
            </div>
            {menuSchema(languageStrings).map((tab: Record<string, any>, i: number)=> (
                <div key={i}>
                    {(!tab.allowedRoles || tab.allowedRoles.includes(sessionUser.active_role.name_en)) && (
                        <UserMenuTab
                            title={tab.title}
                            options={tab.options.map((option: any)=> ({
                                href: option.href,
                                selected: pathname.startsWith(option.pathname),
                                label: option.label,
                                icon: option.icon,
                            }))}
                        />
                    )}
                </div>
            ))}
        </div>
    )
}
