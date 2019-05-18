FROM python:3

WORKDIR /spq_server

# copy server files
COPY server/ ./

# install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# configure connection to database
# REPLACE 172.18.0.1 with address of MySQL
RUN sed -i -e 's/"HOST": "127.0.0.1",/"HOST": "172.18.0.1",/g' ./spq_server/settings.py

# entrypoint
CMD [ "python", "manage.py", "runserver" ]
