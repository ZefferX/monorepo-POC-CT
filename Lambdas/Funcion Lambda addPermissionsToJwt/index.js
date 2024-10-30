const { Client } = require('pg');

exports.handler = async (event) => {
    const client = new Client({
        host: process.env.DB_HOST,  // IP pública de tu instancia EC2
        port: process.env.DB_PORT || 5432,  // Puerto de PostgreSQL, por defecto 5432
        user: process.env.DB_USER,  // Usuario de la base de datos
        password: process.env.DB_PASSWORD,  // Contraseña del usuario de la base de datos
        database: process.env.DB_NAME,  // Nombre de la base de datos
        ssl: false  // No usar SSL
    });

    try {
        console.log("Evento recibido:", JSON.stringify(event, null, 2));

        const email = event.request.userAttributes?.email;
        if (!email) {
            throw new Error("Email no proporcionado.");
        }

        console.log("Iniciando conexión a la base de datos...");
        await client.connect();
        console.log("Conexión a la base de datos exitosa");

        // Consultar permisos en la base de datos PostgreSQL
        const res = await client.query('SELECT permissions FROM users WHERE email = $1', [email]);
        const permissions = res.rows.length > 0 ? res.rows[0].permissions : ["default_permission"];
        
        console.log("Permisos obtenidos:", permissions);

        // Preparar los permisos en formato de lista separada por comas
        const permissionsString = permissions.join(',');

        // Agregar los permisos al ID Token y al Access Token como un atributo personalizado
        event.response = event.response || {};
        event.response.claimsOverrideDetails = event.response.claimsOverrideDetails || {};
        event.response.claimsOverrideDetails.claimsToAddOrOverride = {
            "custom:permissions": permissionsString  // Permisos en formato de string
        };

        console.log("Claims a agregar/override:", event.response.claimsOverrideDetails.claimsToAddOrOverride);

        return event;
    } catch (error) {
        console.error("Error en Lambda:", error);
        throw error;
    } finally {
        await client.end();
    }
};
