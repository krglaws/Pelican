from __future__ import print_function
from flask import Flask, request, send_from_directory, render_template
from werkzeug.utils import secure_filename
import MySQLdb
import sys
import os

UPLOAD_FOLDER = 'uploads'
ALLOWED_EXTENSIONS = {'mp4', 'm4v', 'avi', 'txt'}

app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

db = MySQLdb.connect(host='localhost', user='root', passwd='pelican2018', db='pelican')
cur = db.cursor()


@app.route('/pelican/index', methods=['GET'])
def index():
    cur.execute("SELECT * FROM videos;")
    vids = list(cur.fetchall())
    s = ''
    for item in reversed(vids):
        for val in item:
            s += str(val) + " "
        s += '\n'
    return s


def sort_videos(videos):
    sorted = False        
    i = len(videos)
    for loop1 in range(i):
        sorted = True
        for loop2 in range(i-1):
            if (int(videos[loop2][-1]) < int(videos[loop2+1][-1])):
                temp = videos[loop2]
                videos[loop2] = videos[loop2+1]
                videos[loop2+1] = temp
                sorted = False
        if sorted:
            return videos
                

@app.route('/pelican', methods=['GET', 'POST'])
def main():
    print('Starting...', file=sys.stderr)

    # check request method
    if request.method == 'POST':
        print('Attempting to upload file...', file=sys.stderr)
        username = request.args.get('username')
        userid = request.args.get('userid')
        if (not username or not userid):
            print('No user name or id provided', file=sys.stderr)
            return '''No user name or id provided'''

        # check if upload file was sent
        if 'uploaded_file' not in request.files:
            print('No upload file specified', file=sys.stderr)
            return '''No upload file specified'''

        # grab uploaded file object
        the_file = request.files['uploaded_file']
    
        filename = the_file.filename

        # check if upload file name is empty string
        if filename == '':
            print('Upload file name is empty', file=sys.stderr)
            return '''Upload file name is empty'''

        filename = filename.split('/')[-1]

        print('Upload file name is ' + filename, file=sys.stderr)

        # check if the file name and extension are allowed
        if the_file and allowed_file(the_file.filename):
            print('File name is legal', file=sys.stderr)

            # ensure file name is safe
            #filename = secure_filename(the_file.filename)

            # check if file already exists
            if (file_exists(filename)):
                return '''A file of this name already exists'''
            
            add_video(filename, username, userid)
            the_file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
            return '''Upload successful'''

        else:
            return '''Illegal file name'''

    else:
        filename = request.args.get('filename')
        if filename is None:
            print('File not specified', file=sys.stderr)
            return '''File not specified'''
        if not file_exists(filename):
            print('File does not exist', file=sys.stderr)
            return '''File does not exist'''
        print('Retrieving file '+filename, file=sys.stderr)
        return send_from_directory(app.config['UPLOAD_FOLDER'], filename)


def allowed_file(filename):
    print('Checking if file name is legal...', file=sys.stderr)
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


def file_exists(filename):
    return filename in os.listdir(app.config['UPLOAD_FOLDER'])
    

def add_video(filename, username, userid):
    cur.execute("INSERT INTO videos (video_name, username, userid, timestamp) VALUES('" + filename + "', '" + username + "', '" + userid + "', UNIX_TIMESTAMP());")
    db.commit()


def get_videos():
    curr.execute("SELECT * FROM videos;")
    ls = curr.fetchall()
    s = ''
    for item in ls:
        for val in item:
            s += str(val) + " "
    return s


if __name__ == '__main__':
    app.secret_key = os.urandom(24)
    app.config['SESSION_TYPE'] = 'filesystem'
    app.run(host='0.0.0.0', debug=True, port=5000)

