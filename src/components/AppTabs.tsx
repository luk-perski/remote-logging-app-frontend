import React from 'react';
import { Tab, Tabs } from '@material-ui/core';
import { faCalendarAlt, faDoorOpen, faFileAlt, faList, faProjectDiagram, faSignInAlt, faSignOutAlt, faTasks } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useHistory, useLocation } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { RootState } from '../store/reducers';

export const options = [
    {
        icon: faCalendarAlt,
        label: 'Calendar',
        value: '/calendar',
    },
    {
        icon: faTasks,
        label: 'Tasks',
        value: '/tasks',
    },
    {
        icon: faProjectDiagram,
        label: 'Projects',
        value: '/projects',
    },
    {
        icon: faDoorOpen,
        label: 'Login',
        value: '/login',
    }
];

export const AppTabs = () => {
    const history = useHistory();
    const location = useLocation();
    const state = useSelector((state: RootState) => state);

    const app = state.app;

    if (history.location?.pathname === '/') {
        history.replace('/calendar');
    }

    const handleChange = (event: React.ChangeEvent<{}>, newValue: string) => {
        history.push(newValue);
    };

    return (
        <Tabs
            orientation="vertical"
            indicatorColor="primary"
            textColor="primary"
            value={location.pathname}
            onChange={handleChange}
        >
            {options.map((option) => (
                <Tab
                    key={option.label}
                    icon={<FontAwesomeIcon icon={option.icon} />}
                    label={option.label}
                    value={option.value}
                />
            ))}
        </Tabs>
    );
};