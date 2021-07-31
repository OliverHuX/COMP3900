import React from 'react';
import { List, Tooltip, Comment } from 'antd';
import moment from 'moment';


export default function Comments() {
    const [comments, setComments] = React.useState('')
    
    React.useEffect(() => {
        setComments([
            {
                author: 'Han Solo',
                avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
                content: (
                  <p>
                    We supply a series of design principles, practical patterns and high quality design
                    resources (Sketch and Axure), to help people create their product prototypes beautifully and
                    efficiently.
                  </p>
                ),
                datetime: (
                  <Tooltip title={moment().subtract(1, 'days').format('YYYY-MM-DD HH:mm:ss')}>
                    <span>{moment().subtract(1, 'days').fromNow()}</span>
                  </Tooltip>
                ),
            },
            {
                author: 'Han HHHHHH',
                avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
                content: (
                    <p>
                      We supply a series of design principles, practical patterns and high quality design
                      resources (Sketch and Axure), to help people create their product prototypes beautifully and
                      efficiently.
                    </p>
                ),
                datetime: (
                    <Tooltip title={moment().subtract(1, 'days').format('YYYY-MM-DD HH:mm:ss')}>
                      <span>{moment('2021-05-21 23:34:12').fromNow()}</span>
                    </Tooltip>
                  ),
            },
        ]);
    }, []);

    return (
        <div>
            <List
                // className="comment-list"
                header={`${comments.length} replies`}
                itemLayout="horizontal"
                dataSource={comments}
                renderItem={item => (
                <li>
                    <Comment
                        author={item.author}
                        avatar={item.avatar}
                        content={item.content}
                        datetime={item.datetime}
                    />
                </li>
                )}
            />
        </div>
    )
}