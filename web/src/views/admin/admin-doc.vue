<template>

  <a-layout>
    <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
          <p>
            <a-form
                    layout="inline"
                    :model="param"
            >
              <a-form-item>
                <a-button type="primary"  @click="handleQuery()"  >
                  查询
                </a-button>
              </a-form-item>

              <a-form-item>
                <a-button type="primary"  @click="add()"  >
                  新增
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-table
                  v-if="level1.length > 0"
                  :columns="columns"
                  :row-key="record => record.id"
                  :data-source="level1"
                  :pagination="false"
                  :loading="loading"
                  :defaultExpandAllRows="true"
          >
            <template #name="{ text, record }">
              {{record.sort}} {{text}}
            </template>
            <template v-slot:action="{ text, record }">
              <a-space size="small">
                <a-button type="primary"  @click="edit(record)"  size="small">
                  编辑
                </a-button>
                <a-popconfirm
                        title="删除后不可恢复，确认删除?"
                        ok-text="确认"
                        cancel-text="取消"
                        @confirm="handleDelete(record.id)"
                >
                  <a-button type="danger"  size="small">
                    删除
                  </a-button>
                </a-popconfirm>

              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form :model="doc" layout="vertical">
              <a-form-item >
                <a-button type="primary" @click="handleSave()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" layout="vertical">
            <a-form-item >
              <a-input v-model:value="doc.name"  placeholder="名称"/>
            </a-form-item>
            <a-form-item >
              <a-tree-select
                      v-model:value="doc.parent"
                      style="width: 100%"
                      :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                      :tree-data="treeSelectData"
                      placeholder="请选择父文档"
                      tree-default-expand-all
                      :replaceFields="{title: 'name', key: 'id', value: 'id'}"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item >
              <a-input v-model:value="doc.sort"  placeholder="顺序" />
            </a-form-item>
            <a-form-item >
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>


    </a-layout-content>
  </a-layout>
<!--  <a-modal-->
<!--          title="分类表单"-->
<!--          v-model:visible="modalVisible"-->
<!--          :confirm-loading="modalLoading"-->
<!--          @ok="handleModalOk"-->
<!--  >-->

<!--  </a-modal>-->
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref  , createVNode } from 'vue';
  import axios from 'axios';
  import {message, Modal} from 'ant-design-vue';
  import {Tool as tools} from "@/util/tool";
  import {Tool } from "@/util/tool";
  import {useRoute} from "vue-router";
  import ExclamationCircleOutlined from "@ant-design/icons-vue/ExclamationCircleOutlined";
  import E from 'wangeditor';
  export default defineComponent({
    name: 'AdminDoc',
    setup() {
      const route = useRoute();
      console.log("路由：", route);
      console.log("route.path：", route.path);
      console.log("route.query：", route.query);
      console.log("route.param：", route.params);
      console.log("route.fullPath：", route.fullPath);
      console.log("route.name：", route.name);
      console.log("route.meta：", route.meta);

      const param = ref();
      param.value = {};
      const docs = ref();

      const loading = ref(false);

      const columns = [



        {
          title: '名称',
          dataIndex: 'name',
          slots: { customRender: 'name' }
        },
        {
          title: 'Action',
          key: 'action',
          slots: { customRender: 'action' }
        }
      ];
      /**
       * 一级分类树，children属性就是二级分类
       * [{
       *   id: "",
       *   name: "",
       *   children: [{
       *     id: "",
       *     name: "",
       *   }]
       * }]
       */

      const level1 = ref(); // 一级分类树，children属性就是二级分类
      level1.value = [];
      // level1.value = [];

      /**
       * 数据查询
       **/
      const handleQuery = () => {
        loading.value = true;
        // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
        level1.value = [];
        axios.get("/doc/all").then((response) => {
          loading.value = false;
          const data = response.data;
          if(data.success){
            docs.value = data.content;
            console.log("原始数组：", docs.value);

            level1.value = [];
            level1.value = tools.array2Tree(docs.value, 0);
            console.log("树形结构：", level1);
          }else {
            message.error(data.message);
          }
        });
      };
      /**
       * 内容查询
       **/
      const handleQueryContent = () => {
        axios.get("/doc/find-content/"+doc.value.id).then((response) => {
          const data = response.data;
          if(data.success){
            editor.txt.html(data.content);
          }else {
            message.error(data.message);
          }
        });
      };
      //因为树选择组件的属性状态 会随着当前编辑节点而变化 所以单独声明一个响应式变量 而不使用level1
      const treeSelectData = ref();
      treeSelectData.value = [];
      const doc = ref();
      doc.value={};
      const modalText = ref<string>('Content of the modal');
      const modalVisible = ref<boolean>(false);
      const modalLoading = ref<boolean>(false);
      const editor = new E('#content');
      editor.config.zIndex = 0;//避免下拉框被富文本挡住
      const handleSave = () => {
        // modalText.value = 'The modal will be closed after two seconds';
        modalLoading.value = true;
        // setTimeout(() => {
        //   modalVisible.value = false;
        //   modalLoading.value = false;
        // }, 2000);
        doc.value.content = editor.txt.html();
        axios.post("/doc/save",doc.value).then((response) => {
          modalLoading.value = false;//loading的效果只要后端有返回的时候就应该去除掉 而不是返回成功的时候再去除掉
          const data = response.data;
          if(data.success){
            modalVisible.value = false;
            handleQuery();
          }else{
              message.error(data.message);
          }
        });
      };

      /**
       * 将某节点及其子孙节点全部置为disabled
       */
      const setDisable = (treeSelectData: any, id: any) => {
        // console.log(treeSelectData, id);
        // 遍历数组，即遍历某一层节点
        for (let i = 0; i < treeSelectData.length; i++) {
          const node = treeSelectData[i];
          if (node.id === id) {
            // 如果当前节点就是目标节点
            console.log("disabled", node);
            // 将目标节点设置为disabled
            node.disabled = true;

            // 遍历所有子节点，将所有子节点全部都加上disabled
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
              for (let j = 0; j < children.length; j++) {
                setDisable(children, children[j].id)
              }
            }
          } else {
            // 如果当前节点不是目标节点，则到其子节点再找找看。
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
              setDisable(children, id);
            }
          }
        }
      };
      /***
       * 编辑
       * @param record
       */
      const edit = (record : any) => {
        modalVisible.value = true;
        doc.value = tools.copy(record);
        handleQueryContent();
        // 不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
        treeSelectData.value = Tool.copy(level1.value);
        setDisable(treeSelectData.value, record.id);

        // 为选择树添加一个"无" unshift是往数组前面添加一个元素 而push是往数组后面添加一个元素
        treeSelectData.value.unshift({id: 0, name: '无'});

      };
      /***
       * 新增
       */
      const add = () => {
        modalVisible.value = true;
        doc.value = { ebookId: route.query.ebookId};

        treeSelectData.value = Tool.copy(level1.value) || [];

        // 为选择树添加一个"无"
        treeSelectData.value.unshift({id: 0, name: '无'});

      };

      // const ids : Array<string> = [];
      const deleteIds: Array<string> = [];
      const deleteNames: Array<string> = [];
      /***
       * 删除
       */
      //const handleDelete = ( id : number ) => {
      const handleDelete = (id: number) => {
        console.log("删除ID "+level1.value,id);
        deleteIds.length = 0;
        deleteNames.length = 0;
        getDeleteIds(level1.value,id);
        console.log("删除ID "+id);
        //axios.delete("/doc/delete/1,2,3"+id).then((response) => {
        Modal.confirm({
          title: '重要提醒',
          icon: createVNode(ExclamationCircleOutlined),
          content: '将删除：【' + deleteNames.join("，") + "】删除后不可恢复，确认删除？",
          onOk() {
            axios.delete("/doc/delete/"+deleteIds.join(",")).then((response) => {
              const data = response.data;
              if(data.success){
                handleQuery();
              } else {
                message.error(data.message);
              }
            });
          },
        });
      };

      /**
       * 将某节点及其子孙节点全部置为disabled
       */
      const getDeleteIds = (treeSelectData: any, id: any) => {
        // console.log(treeSelectData, id);
        // 遍历数组，即遍历某一层节点
        for (let i = 0; i < treeSelectData.length; i++) {
          const node = treeSelectData[i];
          if (node.id === id) {
            // 如果当前节点就是目标节点
            console.log("disabled", node);
            // 将目标节点设置为disabled
            // node.disabled = true;
            //ids.push(id);
            deleteIds.push(id);
            deleteNames.push(node.name);
            // 遍历所有子节点，将所有子节点全部都加上disabled
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
              for (let j = 0; j < children.length; j++) {
                getDeleteIds(children, children[j].id)
              }
            }
          } else {
            // 如果当前节点不是目标节点，则到其子节点再找找看。
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
              getDeleteIds(children, id);
            }
          }
        }
      };
      onMounted(() => {
        handleQuery();
        editor.create();
      });


      return {
        level1,
        //docs,
        columns,
        loading,
        doc,

        modalVisible,
        modalLoading,
        handleSave,

        edit,
        add,
        handleDelete,

        handleQuery,
        param,

        treeSelectData,
      }
    }
  });
</script>
