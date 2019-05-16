FROM python:3

WORKDIR /usr/src/app

# copy server files
COPY server/ ./

# install dependencies
RUN apt-get install sed && pip install --no-cache-dir -r requirements.txt

# configure connection to database
RUN echo $(ls .)
RUN sed -i -e 's/"HOST": "127.0.0.1",/"HOST": "192.168.0.1",/g' ./spq_server/settings.py

# prepare database
#RUN python manage.py makemigrations && \
#    python manage.py migrate

# entrypoint
CMD [ "python", "manage.py", "runserver" ]
