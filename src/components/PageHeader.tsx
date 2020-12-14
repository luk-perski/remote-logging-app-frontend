import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';

export const PageHeader = ({ href, header }: { href?: string, header: string })=> {
    return (
        <div className="flex my-4 text-xl text-center">
            {!!href && (
                <Link className="flex items-center" to={href}>
                    <FontAwesomeIcon icon={faArrowLeft} className="left-0 cursor-pointer mr-4" />
                </Link>
            )}
            <span className="text-2xl font-bold">
                {header}
            </span>
        </div>
    )
}
