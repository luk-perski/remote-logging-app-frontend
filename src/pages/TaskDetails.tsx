import { CircularProgress } from '@material-ui/core';
import { TextField } from '@material-ui/core';
import { Typography } from '@material-ui/core';
import { useTheme, useMediaQuery } from '@material-ui/core';
import dayjs from 'dayjs';
import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useParams } from 'react-router-dom';
import { ITextField } from '../components/ITextField';
import { PageTitle } from '../components/PageTitle';
import { getTask } from '../store/actions/tasks';
import { RootState } from '../store/reducers';

interface RouteParams {
    taskId: string
}

export const TaskDetails = () => {
    const state = useSelector((state: RootState) => state);
    const params = useParams<RouteParams>();
    const dispatch = useDispatch();
    const theme = useTheme();
    const matches = useMediaQuery(theme.breakpoints.up('md'));
    const humanizeDuration = require("humanize-duration");

    const task = state.tasks.selectedTaskRequest;
    const loadingTaskRequest = state.tasks.loadingTaskRequest;
    const taskId = Number(params.taskId)

    if (!loadingTaskRequest && (!task || task.id !== taskId)) {
        dispatch(getTask(state, taskId));
    }

    return (
        <>
            <PageTitle title={`Task - ${task?.name}`} />
            {loadingTaskRequest ? (
                <div className="ml-64 mt-6">
                    <CircularProgress />
                </div>
            ) : (
                    <>
                        <div className="flex-col">
                            <Typography className="p-6" variant="h5">{task?.name}</Typography>
                            <div className="flex flex-row flex-wrap">
                                <div className="p-10">
                                    <ITextField
                                        labelText="Description"
                                        value={task?.description}
                                        maxRows={32}
                                        fullWidth={true}
                                        multiline={true}
                                    />
                                    <ITextField
                                        labelText="Category"
                                        value={task?.category?.name}
                                    />
                                </div>
                                <div className="p-10 flex flex-col">
                                    <ITextField
                                        labelText="Assignee"
                                        value={task?.assigneeName}
                                    />
                                    <ITextField
                                        labelText="Reporter"
                                        value={task?.creatorName}
                                    />
                                    <ITextField
                                        labelText="Project"
                                        value={task?.projectName}
                                    />
                                    <ITextField
                                        labelText="Time spent"
                                        value={task?.timeSpent ? String(humanizeDuration(task?.timeSpent)) : "No time logged"}
                                    />
                                    <ITextField
                                        labelText="Estimate"
                                        value={String(humanizeDuration(task?.estimate))}
                                    />
                                    <ITextField
                                        labelText="Created date"
                                        value={dayjs(task?.cratedDate).format('YYYY-MM-DD HH:mm:ss')}
                                    />
                                    {task?.resolverDate ? (
                                        <ITextField
                                            labelText="Resolved date"
                                            value={dayjs(task?.resolverDate).format('YYYY-MM-DD HH:mm:ss')}
                                        />) : (
                                            <>
                                                {task?.runStart ? (
                                                    <ITextField
                                                        labelText="Last start date"
                                                        value={dayjs(task?.runStart).format('YYYY-MM-DD HH:mm:ss')} />
                                                ) : null
                                                }
                                                {task?.runEnd ?
                                                    (<ITextField
                                                        labelText="Last end date"
                                                        value={dayjs(task?.runEnd).format('YYYY-MM-DD HH:mm:ss')}
                                                    />
                                                    ) : null
                                                }
                                            </>
                                        )
                                    }
                                </div>
                            </div>
                        </div>
                    </>
                )}
        </>
    )
}