import React, { useState, MouseEvent } from 'react';
import { Link } from 'react-router-dom';
import { Link as HtmlLink } from '@material-ui/core';
import { useSelector, useDispatch } from 'react-redux';
import { Collapse, Paper, Popover, useMediaQuery, useTheme } from '@material-ui/core';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
    faBars,
    faSignInAlt,
    IconDefinition,
    faPaperPlane,
    faSignOutAlt, faCaretUp, faCaretDown
} from '@fortawesome/free-solid-svg-icons';
import { languages } from '../language-strings';
import { setLanguage } from '../store/actions/app';
import { IButton } from './IButton';
import { RootState } from '../store/reducers';
import { menuSchema } from '../utils/menuSchema';
import { API_URL } from '../config';

const MobileMenuElement = ({ title, titleIcon, options, onClick }: { title: string, titleIcon: IconDefinition, options: any[], onClick?: ()=> void })=> {
    const [opened, setOpened] = useState(true);

    return (
        <div>
            <div
                onClick={()=> setOpened(!opened)}
                className="flex justify-between bg-iscte-blue-dark cursor-pointer items-center px-3 py-2"
            >
                <div>
                    <FontAwesomeIcon className="text-white" icon={titleIcon} />
                    <span className="text-white ml-2">{title}</span>
                </div>
                <div>
                    <FontAwesomeIcon className="text-white" icon={opened? faCaretUp : faCaretDown} />
                </div>
            </div>
            {opened && (
                <div>
                    {options.map((option, i)=> (
                        <Link key={i} to={option.href} onClick={onClick}>
                            <div
                                className="bg-iscte-blue-lighter"
                                style={{ paddingTop: i === 0? '1px' : '', paddingBottom: '1px' }}
                            >
                                <div
                                    className="px-3 bg-iscte-blue-light py-2 flex cursor-pointer items-center"
                                >
                                    <FontAwesomeIcon className="text-white" icon={option.icon} />
                                    <span className="text-white ml-2">{option.label}</span>
                                </div>
                            </div>
                        </Link>
                    ))}
                </div>
            )}
        </div>
    )
}

export const TopBar = ()=> {
    const theme = useTheme();
    const matches = useMediaQuery(theme.breakpoints.up('md'));
    const dispatch = useDispatch();
    const app = useSelector((state: RootState) => state.app);
    const [menuAnchorEl, setMenuAnchorEl] = useState<EventTarget & Element | null>(null);
    const [openedOptions, setOpenedOptions] = useState(false);

    const handleMenuClick = (event: MouseEvent) => {
        setMenuAnchorEl(event?.currentTarget);
    }

    const handleMenuClose = () => {
        setMenuAnchorEl(null);
    }

    const env = app.environment;
    const lang = app.language;
    const languageStrings = app.languageStrings;
    const sessionUser = app.sessionUser;
    const oneDriveStatus = app.oneDriveStatus;
    console.log(oneDriveStatus);
    const isLoggedIn = Boolean(app.token);

    const isProd = env === 'prod';

    if(matches && openedOptions) {
        setOpenedOptions(false);
    }

    function changeLanguage(language: string) {
        dispatch(setLanguage(language));
    }

    const MenuElement = ({ fontAwesomeIcon, text, href }: { fontAwesomeIcon: IconDefinition | undefined, text: string, href: string })=> (
        <div>
            <Link to={href}>
                <IButton className="p-0">
                    <div className={`flex items-center ${isProd? 'bg-white text-black' : 'bg-iscte-black text-white'} h-10 w-40 px-6`}>
                        {fontAwesomeIcon && <FontAwesomeIcon icon={fontAwesomeIcon} />}
                        <span className="ml-1 capitalize">{text}</span>
                    </div>
                </IButton>
            </Link>
        </div>
    )
    
    const TestEnvironmentFlag = ({ className }: { className?: string })=> (
        <div>
            <span
                className={`${matches? 'mx-6' : ''} px-2 my-1 py-1 bg-iscte-yellow
                    text-black font-bold uppercase ${className}`}
            >
                {languageStrings.test_environment}
            </span>
        </div>
    );

    return (
        <div>
            {oneDriveStatus && oneDriveStatus !== 'ok' && (
                <div style={{ minHeight: '24px' }} className="py-3 px-6 bg-iscte-warning-yellow border-iscte-warning-yellow iscte-warning-yellow">
                    Onedrive isn't configured or needs to be renewed. <HtmlLink href={`${API_URL}/login/onedrive`}>Click here</HtmlLink> to login.
                </div>
            )}
            <div className={`flex justify-between w-auto ${matches? 'px-8 py-3' : 'p-4'} ${isProd? 'bg-iscte-grey text-black' : 'bg-iscte-green text-white'}`}>
                <div className={`flex ${matches? 'items-center' : 'flex-col justify-between'} min-h-20`}>
                    <div>
                        <a href="https://www.iscte-iul.pt/">
                            <img
                                className={`${matches? 'h-10': 'h-8'} cursor-pointer`}
                                src={`/images/logo_iscte_${isProd ? 'pos' : 'neg'}_detailed_${languageStrings.TopBar.imageTag}.svg`}
                                alt="iscte-logo"
                            />
                        </a>
                    </div>
                    {matches && (
                        <div
                            className={`h-6 ${isProd ? 'bg-black' : 'bg-white'} w-3px m-4`}
                        />
                    )}
                    <div className={matches? '' : 'mt-2'}>
                        <Link to="/">
                            <span className={`${matches? 'text-xl' : 'text-l'} font-bold cursor-pointer`}>
                                {languageStrings.TopBar.title1}
                                <span className="mx-2">
                                    <FontAwesomeIcon icon={faPaperPlane} />
                                </span>
                                {languageStrings.TopBar.title2}
                            </span>
                        </Link>
                    </div>
                    {!isProd && matches && (
                        <TestEnvironmentFlag className="text-sm" />
                    )}
                </div>
                {matches? (
                    <div className="flex items-center text-sm">
                        <div className="flex items-center mx-8">
                            <span
                                className={
                                    lang === languages.PORTUGUESE?
                                        `${isProd ? 'iscte-grey-dark' : 'text-black'}` :
                                        `${isProd ? 'text-black' : 'text-white'} cursor-pointer`
                                }
                                onClick={() => changeLanguage(languages.PORTUGUESE)}
                            >
                                PT
                            </span>
                            <div
                                className={`h-3 ${isProd ? 'bg-black' : 'bg-white'} w-2px mx-2`}
                            />
                            <span
                                className={
                                    lang === languages.ENGLISH?
                                        `${isProd ? 'iscte-grey-dark' : 'text-black'}` :
                                        `${isProd ? 'text-black' : 'text-white'} cursor-pointer`
                                }
                                onClick={() => changeLanguage(languages.ENGLISH)}
                            >
                                EN
                            </span>
                        </div>
                        <div className="flex items-center cursor-pointer">
                            <div onClick={handleMenuClick}>
                                <FontAwesomeIcon icon={faBars} />
                                <span className="ml-1">
                                    {languageStrings.menu}
                                </span>
                            </div>
                            <Popover
                                anchorEl={menuAnchorEl}
                                anchorOrigin={{
                                    vertical: 'bottom',
                                    horizontal: 'right',
                                }}
                                transformOrigin={{
                                    vertical: 'top',
                                    horizontal: 'right',
                                }}
                                open={Boolean(menuAnchorEl)}
                                onClose={handleMenuClose}
                            >
                                {/* <MenuElement fontAwesomeIcon={faQuestionCircle} text={'FAQs'} href={'/faq'} />
                                <MenuElement fontAwesomeIcon={faEnvelope} text={'Help'} href={'/help'} />
                                <div style={{ height: '4px' }} className="bg-gray-800" /> */}
                                {isLoggedIn? (
                                    <MenuElement fontAwesomeIcon={faSignInAlt} text={'Logout'} href={'/logout'} />
                                ) : (
                                    <MenuElement fontAwesomeIcon={faSignInAlt} text={'Sign In'} href={'/login'} />
                                )}
                            </Popover>
                        </div>
                    </div>
                ) : (
                    <div onClick={()=> setOpenedOptions(!openedOptions)} className="flex cursor-pointer flex-col items-end justify-between">
                        <span className="border py-1 px-2">
                            <FontAwesomeIcon icon={faBars} />
                        </span>
                        <TestEnvironmentFlag className="text-xs" />
                    </div>
                )}
            </div>
            <Collapse in={openedOptions}>
                <Paper
                    style={{ paddingTop: '4px' }}
                    classes={{ root: 'bg-iscte-blue-light rounded-none' }}
                >
                    <div
                        onClick={() => changeLanguage(languages.PORTUGUESE)} className="flex bg-iscte-blue-dark cursor-pointer items-center px-3 py-2"
                    >
                        <img style={{ height: '11px' }} src="/images/pt.png" alt="en" />
                        <span className="ml-2 text-white">Português</span>
                    </div>
                    <div
                        style={{ marginTop: '1px' }}
                        onClick={() => changeLanguage(languages.ENGLISH)} className="flex bg-iscte-blue-dark cursor-pointer items-center px-3 py-2"
                    >
                        <img style={{ height: '11px' }} src="/images/en.png" alt="en" />
                        <span className="ml-2 text-white">English</span>
                    </div>
                    {isLoggedIn? (
                        <Link to={'/logout'}>
                            <div
                                style={{ marginTop: '1px', marginBottom: '3px' }}
                                className="flex bg-iscte-blue-dark cursor-pointer items-center px-3 py-2"
                            >
                                <FontAwesomeIcon className="text-white" icon={faSignOutAlt} />
                                <span className="text-white ml-2">Logout</span>
                            </div>
                        </Link>
                    ) : (
                        <Link to={'/login'}>
                            <div className="flex bg-iscte-blue-dark cursor-pointer items-center mt-1 mb-2 px-3 py-2">
                                <FontAwesomeIcon className="text-white" icon={faSignInAlt} />
                                <span className="text-white ml-2">Login</span>
                            </div>
                        </Link>
                    )}
                    {menuSchema(languageStrings).map((menu: Record<string, any>, i: number)=> (
                        <div key={i}>
                            {menu.allowedRoles?.includes(sessionUser?.active_role.name_en) && (
                                <MobileMenuElement
                                    onClick={()=> setOpenedOptions(false)}
                                    title={menu.title}
                                    titleIcon={menu.titleIcon}
                                    options={menu.options}
                                />
                            )}
                        </div>
                    ))}
                </Paper>
            </Collapse>
        </div>
    );
}
