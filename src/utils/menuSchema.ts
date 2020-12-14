import { languageStringsByLanguage } from "../language-strings";

export const menuSchema = (languageStrings: typeof languageStringsByLanguage.en | typeof languageStringsByLanguage.pt)=> [];

// Example: 
/* {
    allowedRoles: null,
    title: languageStrings.profile,
    titleIcon: faUser,
    options: [
        {
            href: '/user/profile',
            pathname: '/user/profile',
            label: languageStrings.personal_profile,
            icon: faUser,
        },
        {
            href: '/user/change-role',
            pathname: '/user/change-role',
            label: languageStrings.switch_role,
            icon: faExchangeAlt,
        }
    ]
}, {
    allowedRoles: [userRoles.MANAGER, userRoles.ADMIN],
    title: languageStrings.requests,
    titleIcon: faTruckLoading,
    options: [
        {
            href: '/requests/emails',
            pathname: '/requests/emails',
            label: languageStrings.emails,
            icon: faEnvelope,
        },
        {
            href: '/requests/smses',
            pathname: '/requests/smses',
            label: languageStrings.smses,
            icon: faComment,
        }
    ]
}, {
    allowedRoles: [userRoles.MANAGER, userRoles.ADMIN],
    title: languageStrings.dispatches,
    titleIcon: faTruck,
    options: [
        {
            href: '/dispatches/emails',
            pathname: '/dispatches/emails',
            label: languageStrings.emails,
            icon: faEnvelope,
        },
        {
            href: '/dispatches/smses',
            pathname: '/dispatches/smses',
            label: languageStrings.smses,
            icon: faComment,
        }
    ]
}, {
    allowedRoles: [userRoles.ADMIN],
    title: languageStrings.users,
    titleIcon: faUser,
    options: [
        {
            href: '/manage-users',
            pathname: '/manage-users',
            label: languageStrings.manage_users,
            icon: faUserTag,
        }
    ]
}, {
    allowedRoles: [userRoles.ADMIN],
    title: languageStrings.users,
    titleIcon: faThumbtack,
    options: [
        {
            href: '/api/jobs',
            pathname: '/api/jobs',
            label: languageStrings.jobs,
            icon: faTasks,
        },
        {
            href: '/api/users',
            pathname: '/api/users',
            label: languageStrings.api_users,
            icon: faCloud,
        }
    ]
} */