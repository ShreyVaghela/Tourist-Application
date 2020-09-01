FROM ubuntu:18.04

RUN apt-get update -y && apt-get install -y python3-pip python-dev libmysqlclient-dev mysql-client  

COPY ./requirements.txt /app/requirements.txt

WORKDIR /app

RUN pip3 --no-cache-dir install -r requirements.txt

COPY . /app
ENTRYPOINT ["python3"]

CMD ["app.py"]

EXPOSE 5002
