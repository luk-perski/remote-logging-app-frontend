import React from 'react';
import { Helmet } from 'react-helmet';

interface PageTitleProps {
    title: string;
}

export const PageTitle = ({ title }: PageTitleProps)=> {
    return (
        <Helmet>
            <title>{title} - Remote Logging</title>
        </Helmet>
    )
};
