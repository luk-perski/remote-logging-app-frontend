import React, { useEffect, useState } from 'react';
import { CircularProgress, Link } from '@material-ui/core';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../store/reducers';
import { getTasks } from '../store/actions/tasks';
import { Typography } from '@material-ui/core';
import { TableContainer } from '@material-ui/core';
import { Table } from '@material-ui/core';
import { TableHead } from '@material-ui/core';
import { TableCell } from '@material-ui/core';
import { TableBody } from '@material-ui/core';
import { TableRow } from '@material-ui/core';
import dayjs from 'dayjs';
import { PageTitle } from '../components/PageTitle';
import { pages } from '../utils/pages';
import { Redirect } from 'react-router-dom';


const TaskResultRow = (
    {
        task,
        onClick,
        onClose
    }: {
        task: JsonSchema.ModelApiTask,
        onClick: () => void,
        onClose: () => void
    }
) => {
    return (<div>TaskResult</div>)
};


export const Tasks = () => {
    const [redirect, setRedirect] = useState<number | undefined>(undefined);

    const state = useSelector((state: RootState) => state);
    const dispatch = useDispatch();
    const tasks = state.tasks;
    const tasksList = tasks.tasksList;
    const loadingTasks = tasks.loadingTasks;

    useEffect(() => {
        dispatch(getTasks());
    }, [dispatch]);

    if (redirect) {
        console.log(state);
        return <Redirect push to={pages.taskDetails.url(redirect.toString())} />
    }

    return (
        <div className="p-6">
            <PageTitle title="Tasks" />
            <Typography variant="h4" component="h4">Tasks</Typography>
            <div>
                {loadingTasks || !tasksList ? (
                    <div className="text-center">
                        <CircularProgress />
                    </div>
                ) : (
                        <TableContainer>
                            <Table>
                                <TableHead>
                                    <TableCell>
                                        ID
                                        </TableCell>
                                    <TableCell>
                                        Name
                                        </TableCell>
                                    <TableCell>
                                        Status
                                        </TableCell>
                                    <TableCell>
                                        Project
                                        </TableCell>
                                    <TableCell>
                                        Assignee
                                        </TableCell>
                                    <TableCell>
                                        Created Date
                                        </TableCell>
                                </TableHead>
                                <TableBody>
                                    {tasksList?.map((task: JsonSchema.ModelApiTask) => (
                                        <TableRow
                                            key={task.id}
                                            hover={true}
                                            onClick={() => setRedirect(task.id)}
                                        >
                                            <TableCell>
                                                {task.id}
                                            </TableCell>
                                            <TableCell>
                                                {task.name}
                                            </TableCell>
                                            <TableCell>
                                                {task.taskStatus}
                                            </TableCell>
                                            <TableCell>
                                                {task.projectName}
                                            </TableCell>
                                            <TableCell>
                                                {task.assigneeName}
                                            </TableCell>
                                            <TableCell>
                                                {dayjs(task.cratedDate).format('YYYY-MM-DD HH:mm:ss')}
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>

                    )
                }
            </div>
        </div>
    )
}


