import { CircularProgress } from '@material-ui/core';
import { Typography } from '@material-ui/core';
import { useTheme, useMediaQuery } from '@material-ui/core';
import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useParams } from 'react-router-dom';
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

    const task = state.tasks.selectedTaskRequest;
    const loadingTaskRequest = state.tasks.loadingTaskRequest;
    const taskId = Number(params.taskId)

    if (!loadingTaskRequest && (!task || task.id !== taskId)) {
        dispatch(getTask(state, taskId));
    }

    return (
        <>
            <PageTitle title={`Task ${task?.name}`} />
            {loadingTaskRequest ? (
                <div className="ml-64 mt-6">
                    <CircularProgress />
                </div>
            ) : (
                    <>
                        <div className="p-6">
                        <Typography variant="h4" component="h4">{task?.name}</Typography>
                            <form
                                className='validate-form mb-3'
                                onSubmit={(event) => {
                                    event.preventDefault()

                                }}
                            >
                                <div className="row">
                                    <label>Name</label>
                                    <input
                                        type="text"
                                        name="name"
                                        value={task?.name}
                                    // onChange={handleInputChange}
                                    />
                                </div>
                                <div className="row">
                                    <label>Assignee</label>
                                    <input
                                        type="text"
                                        name="username"
                                        value={task?.assigneeName}
                                    // onChange={handleInputChange}
                                    />
                                </div>
                                <div className="row">
                                    <button>Log time</button>
                                </div>

                            </form>
                        </div>
                    </>

                )}
        </>
    )
}