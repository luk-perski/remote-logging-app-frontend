import { CircularProgress, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, FormControl, Input, InputLabel, TextField } from '@material-ui/core';
import { Button } from '@material-ui/core';
import { Typography } from '@material-ui/core';
import { useTheme, useMediaQuery } from '@material-ui/core';
import dayjs from 'dayjs';
import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useParams } from 'react-router-dom';
import { IButton } from '../components/IButton';
import { ITextField } from '../components/ITextField';
import { PageTitle } from '../components/PageTitle';
import { getTask, handleSetDialogField, setOpenLogDialog as setOpenLogDialog, assignUser, startProgress, suspendTask, setOpenAssignDialog, addLogWork } from '../store/actions/tasks';
import { RootState } from '../store/reducers';
import { ITextInput } from '../components/ITextInput';
import { USER_ID } from '../utils/lockrKeys';

interface RouteParams {
    taskId: string
}

export const TaskDetails = () => {
    const dispatch = useDispatch();
    const params = useParams<RouteParams>();
    const state = useSelector((state: RootState) => state);
    const theme = useTheme();
    const matches = useMediaQuery(theme.breakpoints.up('md'));
    const humanizeDuration = require("humanize-duration");
    const Lockr = require("lockr");

    const tasks = state.tasks
    const userId = Lockr.get(USER_ID)     //change it!!!
    const task = tasks.selectedTaskRequest;
    const loadingTaskRequest = tasks.loadingTaskRequest;
    const taskId = Number(params.taskId)
    const logWork = tasks.logWorkToAdd;
    const days = tasks.logWorkDays;
    const hours = tasks.logWorkHours;
    const minutes = tasks.logWorkMinutes;
    const userToAssignId = tasks.userToAssignId;
    const logWorkComment = tasks.logWorkComment;

    if (!loadingTaskRequest && (!task || task.id !== taskId)) {
        dispatch(getTask(taskId));
    }

    // const [open, setOpen] = React.useState(false);
    const openLogDialog = tasks.openLogDialog;
    const openAssignDialog = tasks.openAssignDialog;

    const handleClickLogDialogOpen = () => {
        dispatch(setOpenLogDialog(true));
    };

    const handleCloseLogDialog = () => {
        dispatch(setOpenLogDialog(false));
    };


    const handleSubmitLogDialog = () => {
        dispatch(setOpenLogDialog(false));

        dispatch(addLogWork(taskId, userId, days, hours, minutes, logWorkComment));
    };

    const handleClickAssignDialogOpen = () => {
        dispatch(setOpenAssignDialog(true));
    };

    const handleCloseAssignDialog = () => {
        dispatch(setOpenAssignDialog(false));
    };


    const handleSubmitAssignDialog = () => {
        dispatch(setOpenAssignDialog(false));

        dispatch(assignUser(taskId, userToAssignId));
    };

    // ask why I need use there dispatch()
    const handleDialogFieldChange = (field: string) => (event: React.ChangeEvent<HTMLInputElement>) => {
        dispatch(handleSetDialogField(field, event.target.value));
    };

    const handleAssign = () => {
        assignUser(taskId, 4)
    }

    //todo ask Artur why I need here dispatch 
    const handleStartProgress = () => {
        dispatch(startProgress(taskId, userId));
    }

    const handleSupspend = () => {
        dispatch(suspendTask(taskId, userId));
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
                        <div className="flex-col p-6">
                            <div className="flex">
                                <Typography variant="h5">{task?.name}</Typography>
                                <Typography className={"m-1"} variant="button">{task?.taskStatus}</Typography>
                            </div>
                            <div className="py-3">
                                <IButton onClick={handleClickAssignDialogOpen}>Assign</IButton>
                                <IButton onClick={handleClickLogDialogOpen}>Log work</IButton>
                                {userId === task?.assigneeId ?
                                    <>
                                        {/* ask why I have logs after login, TabsBar, App in cosole after click buttons */}
                                        {task?.taskStatus === "NEW" || task?.taskStatus === "SUSPEND" ?
                                            <IButton onClick={handleStartProgress}>Start progress</IButton>
                                            : <IButton onClick={handleSupspend} isSecondary={true}>Supspend</IButton>}
                                    </>
                                    : null}

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
                                        labelText="Priority"
                                        value={task?.priority}
                                    />
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
                        {/* todo extract this to component */}
                        <Dialog open={openLogDialog} onClose={handleCloseLogDialog} aria-labelledby="form-dialog-title">
                            <DialogTitle id="form-dialog-log-work">Log work</DialogTitle>
                            <DialogContent>
                                <DialogContentText>
                                    Log time to task {task?.name}:
                               </DialogContentText>
                                {/* todo add nicer pickers */}
                                <div className="flex flex-col w-1/3 m-auto">
                                    <ITextInput
                                        className={"m-1"}
                                        labelText="Days"
                                        type="number"
                                        value={days}
                                        onChange={handleDialogFieldChange("days")}
                                    />
                                    <ITextInput
                                        className={"m-1"}
                                        labelText="Hours"
                                        type="number"
                                        value={hours}
                                        onChange={handleDialogFieldChange("hours")}

                                    />
                                    <ITextInput
                                        className={"m-1"}
                                        labelText="Minutes"
                                        type="number"
                                        value={minutes}
                                        onChange={handleDialogFieldChange("minutes")}
                                    />
                                </div>

                                {/* <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                <TimePicker
                                    onChange={void 0}
                                    value={value}
                                />
                                </MuiPickersUtilsProvider> */}
                            </DialogContent>
                            <DialogActions>
                                <Button onClick={handleCloseLogDialog} color="primary">
                                    Cancel
                                </Button>
                                <Button onClick={handleSubmitLogDialog} color="primary">
                                    Submit
                              </Button>
                            </DialogActions>
                        </Dialog>
                        {/* todo extract this to component */}
                        <Dialog open={openAssignDialog} onClose={handleCloseAssignDialog} aria-labelledby="form-dialog-title">
                            <DialogTitle id="form-dialog-assign">Assign to task</DialogTitle>
                            <DialogContent>
                                <DialogContentText>
                                    Assign person to {task?.name}:
                               </DialogContentText>
                                {/* todo change to chose user from team */}
                                <div className="flex flex-col w-1/3 m-auto">
                                    <ITextInput
                                        className={"m-1"}
                                        labelText="Choose person"
                                        type="number"
                                        value={userToAssignId > -1 ? userToAssignId : undefined}
                                        onChange={handleDialogFieldChange("userToAssignId")}
                                    />
                                </div>
                                <ITextInput
                                    className={"m-1"}
                                    labelText="Comment"
                                    value={logWorkComment}
                                    onChange={handleDialogFieldChange("logWorkComment")}
                                    maxRows={32}
                                    fullWidth={true}
                                    multiline={true}
                                />
                            </DialogContent>
                        <DialogActions>
                            <Button onClick={handleCloseAssignDialog} color="primary">
                                Cancel
                                </Button>
                            <Button onClick={handleSubmitAssignDialog} color="primary">
                                Assign
                              </Button>
                        </DialogActions>
                    </Dialog>
                    </>
    )
}
        </>
    )
}