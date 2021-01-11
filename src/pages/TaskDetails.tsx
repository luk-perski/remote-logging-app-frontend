import { CircularProgress, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@material-ui/core';
import { Button } from '@material-ui/core';
import { Typography } from '@material-ui/core';
import { useTheme, useMediaQuery } from '@material-ui/core';
import { MuiPickersUtilsProvider, TimePicker } from '@material-ui/pickers';
import dayjs from 'dayjs';
import React from 'react';
import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useParams } from 'react-router-dom';
import { IButton } from '../components/IButton';
import { ITextField } from '../components/ITextField';
import { PageTitle } from '../components/PageTitle';
import { getTask } from '../store/actions/tasks';
import { RootState } from '../store/reducers';
import DateFnsUtils from '@date-io/date-fns';
import { ITextInput } from '../components/ITextInput';

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

    //todo add to TaskDetailsState
    const [open, setOpen] = useState(false);
    // const [value, onChange] = useState(new Date(Date.now()));

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };


    const handleSubmit = () => {
        setOpen(false);
    };

    return (
        <>
            <PageTitle title={`Task - ${task?.name}`} />
            {loadingTaskRequest ? (
                <div className="ml-64 mt-6">
                    <CircularProgress />
                </div>
            ) : (
                    <>
                        <div className="flex-col p-6">
                            <Typography className="" variant="h5">{task?.name}</Typography>
                            <div className="py-3">
                                <IButton onClick={handleClickOpen}>Log work</IButton>
                                <IButton onClick={() => { alert('clicked') }}>Start progress</IButton>
                                <IButton onClick={() => { alert('clicked') }} isSecondary={true}>Stop progress</IButton>

                            </div>
                            <div className="flex flex-row flex-wrap pt-10">
                                <div className="flex-col">
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
                                <div className="flex flex-col">
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
                                        labelText="Created date"
                                        value={dayjs(task?.cratedDate).format('YYYY-MM-DD HH:mm:ss')}
                                    />

                                </div>
                                <div className="flex flex-col">
                                    <ITextField
                                        labelText="Time spent"
                                        value={task?.timeSpent ? String(humanizeDuration(task?.timeSpent)) : "No time logged"}
                                    />
                                    <ITextField
                                        labelText="Estimate"
                                        value={String(humanizeDuration(task?.estimate))}
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
                        <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
                            <DialogTitle id="form-dialog-title">Log work</DialogTitle>
                            <DialogContent>
                                <DialogContentText>
                                    Log time to task {task?.name}:
                               </DialogContentText>
                               {/* todo add nicer pickers */}
                                <div className="flex flex-col w-1/3 m-auto">
                                    <ITextInput
                                    labelText="Days"
                                    type="number"
                                />
                                    <ITextInput
                                        labelText="Hours"
                                        type="number"
                                    />
                                    <ITextInput
                                        labelText="Minutes"
                                        type="number"
                                    /></div>

                                {/* <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                <TimePicker
                                    onChange={void 0}
                                    value={value}
                                />
                                </MuiPickersUtilsProvider> */}
                            </DialogContent>
                            <DialogActions>
                                <Button onClick={handleClose} color="primary">
                                    Cancel
                                </Button>
                                <Button onClick={handleSubmit} color="primary">
                                    Submit
                              </Button>
                            </DialogActions>
                        </Dialog>
                    </>
                )
            }
        </>
    )
}