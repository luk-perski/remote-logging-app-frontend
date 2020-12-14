import React, { SyntheticEvent } from 'react';

export const IFrame = ({ html, title }: { html: string, title: string })=> {
    const insertHtml = (e: SyntheticEvent<HTMLIFrameElement, Event>)=> {
        const current = e.currentTarget;

        if(!current) {
            return;
        }

        const iframedoc = current.contentDocument || current?.contentWindow?.document;

        if(!iframedoc) {
            return;
        }

        iframedoc.open();
        iframedoc.write(html);
        iframedoc.close();
    };

    return (
        <iframe onLoad={insertHtml} title={title}></iframe>
    );
}
