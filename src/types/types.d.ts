declare namespace JsonSchema {
    export interface ModelApiTask {
        id?: number;
        project?: ModelApiProject;
        name?: string;
        creator?: ModelsApiUser;
        priority?: number;
        assignee?: ModelsApiUser;
        category?: ModelApiCategory;
        cratedDate?: string; //date-time
        description?: string;
        estimate?: number;
        timeSpent?: number;
        resolverDate?: string;
        runStart?: string;
        runEnd?: string;

    }
    export interface ModelsApiUser {
        id?: string;
        name?: string;
        display_name?: string;
        username?: string;
        email?: string;
        local_pwd?: string;
        roles?: ModelApiUserRole[];
        team?: ModelApiTeam;
    }

    export interface ModelApiCategory {
        id?: number;
        name?: string;
        createdDate?: string; //date-time
    }

    export interface ModelApiProject {
        id?: number;
        name?: string;
        manager?: ModelsApiUser;
        isActive?: boolean;
        description?: string;
        createdDate?: string; //date-time
        startDate?: string;
        endDate?: string;
    }

    export interface ModelApiUserRole {
        id?: number;
        user?: ModelsApiUser;
        role?: ModelApiRole;
        is_active?: boolean;
    }

    export interface ModelApiRole {
        id?: number;
        label_pt?: string;
        label_eng?: string;
        short_label_pt?: string;
        short_label_eng?: string;
    }

    export interface ModelApiTeam {
        id?: number;
        name?: string;
        manager?: ModelApiUser;
        createdDate?: string; //date-time
    }
}