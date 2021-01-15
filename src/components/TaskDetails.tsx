import dayjs from 'dayjs';
import React from 'react';
import { ITextField } from './ITextField';

export const ITaskDetails = ({ task, disabled, editable }: { task: JsonSchema.ModelApiTask | null, disabled?: boolean, editable?: boolean }) => {
    const humanizeDuration = require("humanize-duration");
    return (
        <div className="flex flex-row flex-wrap pt-10">
            <div className="flex-col">
                <ITextField
                    labelText="Description"
                    value={task?.description}
                    maxRows={32}
                    fullWidth={true}
                    multiline={true}
                    disabled={disabled}

                />
                <ITextField
                    labelText="Category"
                    value={task?.category?.name}
                    disabled={disabled}
                />
            </div>
            <div className="flex flex-col">
                <ITextField
                    labelText="Priority"
                    value={task?.priority}
                    disabled={disabled}
                />
                <ITextField
                    labelText="Assignee"
                    value={task?.assigneeName}
                    disabled={disabled}
                />
                <ITextField
                    labelText="Reporter"
                    value={task?.creatorName}
                    disabled={disabled}
                />
                <ITextField
                    labelText="Project"
                    value={task?.projectName}
                    disabled={disabled}
                />
                <ITextField
                    labelText="Created date"
                    value={dayjs(task?.cratedDate).format('YYYY-MM-DD HH:mm:ss')}
                    disabled={disabled}
                />

            </div>
            <div className="flex flex-col">
                <ITextField
                    labelText="Time spent"
                    value={task?.timeSpent ? String(humanizeDuration(task?.timeSpent)) : "No time logged"}
                    disabled={disabled}
                />
                <ITextField
                    labelText="Estimate"
                    value={String(humanizeDuration(task?.estimate))}
                    disabled={disabled}
                />
                {task?.resolverDate ? (
                    <ITextField
                        labelText="Resolved date"
                        value={dayjs(task?.resolverDate).format('YYYY-MM-DD HH:mm:ss')}
                        disabled={disabled}
                    />) : (
                        <>
                            {task?.runStart ? (
                                <ITextField
                                    labelText="Last start date"
                                    value={dayjs(task?.runStart).format('YYYY-MM-DD HH:mm:ss')}
                                    disabled={disabled} />
                            ) : null
                            }
                            {task?.runEnd ?
                                (<ITextField
                                    labelText="Last end date"
                                    value={dayjs(task?.runEnd).format('YYYY-MM-DD HH:mm:ss')}
                                    disabled={disabled}
                                />
                                ) : null
                            }
                        </>
                    )
                }
            </div>
        </div>
    );
}