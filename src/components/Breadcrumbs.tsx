import React from 'react';
import { Link } from 'react-router-dom';

interface Breadcrumb {
    href?: string;
    text: string | number;
}

interface Props {
    breadcrumbs: Breadcrumb[];
}

export const Breadcrumbs = ({ breadcrumbs }: Props)=> {
    if(!breadcrumbs.length) {
        return <></>;
    }

    return (
        <div className="mt-8">
            {breadcrumbs.map((breadcrumb, i)=> (
                <span key={i}>
                    {i !== 0 && <span className="mx-2">{'>'}</span>}
                        {breadcrumb.href? (
                            <Link to={breadcrumb.href}>
                                <span className="iscte-blue">{breadcrumb.text}</span>
                            </Link>
                        ) : (
                            <>{breadcrumb.text}</>
                        )}
                </span>
            ))}
        </div>
    )
}
