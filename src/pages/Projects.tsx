import React, { useEffect } from 'react';
import { CircularProgress } from '@material-ui/core';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../store/reducers';
import { Typography } from '@material-ui/core';
import { TableContainer } from '@material-ui/core';
import { Table } from '@material-ui/core';
import { TableHead } from '@material-ui/core';
import { TableCell } from '@material-ui/core';
import { TableBody } from '@material-ui/core';
import { TableRow } from '@material-ui/core';
import dayjs from 'dayjs';
import { getProjects } from '../store/actions/projects';
import { PageTitle } from '../components/PageTitle';


export const Projects = () => {
    const state = useSelector((state: RootState) => state);
    const dispatch = useDispatch();
    const projects = state.projects;
    const projectsList = projects.projectsList;
    const loadingProjects = projects.loadingProjects;

    useEffect(() => {
        if (!projectsList) { dispatch(getProjects()); }
    }, [dispatch]);

    return (
        <div className="p-6">
            <PageTitle title="Projects" />
            <Typography variant="h4" component="h4">Projects</Typography>
            <div>
                {loadingProjects && !projectsList ? (
                    <div className="text-center">
                        <CircularProgress />
                    </div>
                ) : (
                        <TableContainer>
                            <Table>
                                <TableHead>
                                    <TableCell>
                                        Name
                    </TableCell>
                                    <TableCell>
                                        Manager Name
                    </TableCell>
                                    <TableCell>
                                        Description
                    </TableCell>
                                    <TableCell>
                                        Active
                    </TableCell>
                                    <TableCell>
                                        Created Date
                    </TableCell>
                                </TableHead>
                                <TableBody>
                                    {projectsList?.map((project: JsonSchema.ModelApiProject) => (
                                        <TableRow>
                                            <TableCell>
                                                {project.name}
                                            </TableCell>
                                            <TableCell>
                                                {project.managerName}
                                            </TableCell>
                                            <TableCell>
                                                {project.description}
                                            </TableCell>
                                            <TableCell>
                                                {project.isActive ? 'Active' : 'Inactive'}
                                            </TableCell>
                                            <TableCell>
                                                {dayjs(project.createdDate).format('YYYY-MM-DD HH:mm:ss')}
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


